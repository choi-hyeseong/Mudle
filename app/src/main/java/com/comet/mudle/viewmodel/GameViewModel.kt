package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.custom.ListLiveData
import com.comet.mudle.model.Chat
import com.comet.mudle.model.LocalUser
import com.comet.mudle.model.Music
import com.comet.mudle.model.ServerUser
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.MusicService
import com.comet.mudle.service.ServerUserService
import com.comet.mudle.web.stomp.StompService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//provide 계열엔 inject 안넣어도 됨
@HiltViewModel
class GameViewModel @Inject constructor(serverUserService: ServerUserService,
                                        private val musicService: MusicService,
                                        localUserService: LocalUserService,
                                        val stompService: StompService) :
    ViewModel() {

    private val user: LocalUser = localUserService.getUser()
    var userLiveData: LiveData<ServerUser> = serverUserService.getUser(user.uuid)
    val musicLiveData: LiveData<Music> = musicService.getMusic()
    val userResponseLiveData: LiveData<String> = serverUserService.getResponseLiveData()
    val musicResponseLiveData: LiveData<String> = musicService.getResponseLiveData()
    val chatLiveData: ListLiveData<Chat> = stompService.chatLiveData
    val serverStatLiveData: LiveData<Boolean> = stompService.serverStatLiveData

    init {
        //초기화시 connect
        connect()
    }

    fun request(url: String) {
        if (url.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                musicService.request(user.uuid, url)
            }
        }
    }

    fun renewMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            musicService.renewMusic()
        }
    }

    fun sendMessage(message: String) {
        stompService.send(message)
    }

    fun connect() {
        stompService.connect()
    }

    override fun onCleared() {
        super.onCleared()
        stompService.close()
    }

}
