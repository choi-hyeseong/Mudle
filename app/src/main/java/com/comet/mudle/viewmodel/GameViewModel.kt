package com.comet.mudle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.custom.ListLiveData
import com.comet.mudle.model.Chat
import com.comet.mudle.model.Music
import com.comet.mudle.model.User
import com.comet.mudle.repository.music.MusicRepository
import com.comet.mudle.repository.stomp.StompRepository
import com.comet.mudle.repository.stomp.callback.MessageCallback
import com.comet.mudle.repository.user.LocalUUIDRepository
import com.comet.mudle.repository.user.UserRepository
import com.comet.mudle.type.MessageType
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//provide 계열엔 inject 안넣어도 됨
@HiltViewModel
class GameViewModel @Inject constructor(private val stompRepository: StompRepository,
                                        private val uuidRepository: LocalUUIDRepository,
                                        private val musicRepository: MusicRepository,
                                        private val userRepository: UserRepository) : ViewModel(), MessageCallback {

    var user : User? = null //추후 갱신되는 유저정보. 그냥 liveData갱신하면서 얘도 갱신하기. 계속해서 IO 요청넣는것 보단 VM에서 갖고 있는게 나을듯
    val userLiveData: MutableLiveData<User> = MutableLiveData()
    val musicLiveData: MutableLiveData<Music> = MutableLiveData()
    val chatLiveData : ListLiveData<Chat> = ListLiveData()

    val errorLiveData : MutableLiveData<String> = MutableLiveData()
    val resultLiveData : MutableLiveData<String> = MutableLiveData()
    val serverStatusLiveData : MutableLiveData<Boolean> = MutableLiveData()


    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            stompRepository.connect(this@GameViewModel)
        }
    }

    fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            stompRepository.disconnect()
        }

    }

    // 갱신되거나 최신 유저 정보 가져오기
    private fun loadUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val uuid = uuidRepository.getUUID()
            if (uuid == null) {
                errorLiveData.postValue("uuid값이 저장되어 있지 않습니다.")
                return@launch
            }

            userRepository.getUser(uuid).onSuccess {
                val result = data.content.toDomain()
                userLiveData.postValue(result)
                user = result
            }.onFailure {
                errorLiveData.postValue("유저 정보를 가져올 수 없습니다.")
            }
        }
    }

    fun request(url: String?) {
        if (!url.isNullOrEmpty()) {

            CoroutineScope(Dispatchers.IO).launch {
                if (user == null) {
                    errorLiveData.postValue("uuid값이 저장되어 있지 않습니다.")
                    return@launch
                }

                musicRepository.requestMusic(user!!.uuid, url).onSuccess {
                    resultLiveData.postValue(data.message)
                }.onFailure {
                    errorLiveData.postValue(this.message())
                }
                loadUser() // 유저의 코인 정보 갱신
            }
        }
    }

    fun loadMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            musicRepository.getMusic().onSuccess {
                musicLiveData.postValue(data.content.toDomain())
            }.onFailure {
                errorLiveData.postValue("음악 정보를 가져오지 못했습니다.")
            }
        }
    }

    fun sendMessage(message: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val uuid = uuidRepository.getUUID()
            if (uuid == null) {
                errorLiveData.postValue("uuid값이 저장되어 있지 않습니다.")
                return@launch
            }

            //현재 백엔드 구조상 유저 정보를 모두 전송하는데..
            if (user != null)
                kotlin.runCatching {
                    stompRepository.write(user!!, message)
                }.onFailure {
                    errorLiveData.postValue("서버와 연결되어 있지 않습니다.")
                }
            else
                errorLiveData.postValue("유저 정보가 로드되지 않았습니다.")
        }

    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch {
            stompRepository.disconnect()
        }
    }

    override fun onOpen() {
        // stomp 연결시 유저 정보 및 음악 정보 로드
        loadUser()
        loadMusic()

        serverStatusLiveData.postValue(true)
    }

    override fun onClose() {
        serverStatusLiveData.postValue(false)
    }

    override fun onMessage(chat: Chat) {
        if (chat.type == MessageType.REQUEST) //새로운 음악 시작시
            loadMusic()
        else if (chat.type == MessageType.UPDATE && user != null && user!!.uuid.toString() == chat.message)
            loadUser()
        else
            chatLiveData.add(chat)

    }

}
