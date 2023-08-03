package com.comet.mudle.web.stomp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.custom.ListLiveData
import com.comet.mudle.model.Chat
import com.comet.mudle.type.MessageType
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.MusicService
import com.comet.mudle.service.ServerUserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import java.util.UUID

class StompService(private val localUserCase: LocalUserService,
                   private val userCase: ServerUserService,
                   private val musicUseCase: MusicService) {

    private lateinit var connection: Disposable
    private lateinit var subscribe: Disposable
    private lateinit var stompClient: StompClient
    private val user = localUserCase.getUser()
    private val mapper = ObjectMapper()
    val chatLiveData: ListLiveData<Chat> = ListLiveData()
    val serverStatLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun connect() {
        Thread {
            val url = "ws://192.168.219.106:8080/ws"
            val intervalMillis = 1000L
            val client = OkHttpClient()
            stompClient = StompClient(client, intervalMillis).let {
                it.url = url
                it
            }
            connection = stompClient.connect().subscribe {
                //connection type
                when (it.type) {
                    Event.Type.OPENED -> {
                        userCase.renewUser(user.uuid)
                        musicUseCase.renewMusic()
                        serverStatLiveData.postValue(true)
                        startSubscribe() //open시 재구독 -> 다시 접속해도 메시지 주고 받을 수 있게
                    }

                    else -> {
                        serverStatLiveData.postValue(false)
                        if (this::subscribe.isInitialized)
                            subscribe.dispose() //구독만 취소 (connection 끊으면 reconnect 안함)
                    }
                }
                Log.i("asd", "${it.type}")
            }

        }.start()
    }

    fun send(message: String) {
        //subscribe 까지
        val mapper = ObjectMapper()
        val chat = Chat(MessageType.USER, user.uuid, user.name, message, System.currentTimeMillis())
        stompClient.send("/pub/message", mapper.writeValueAsString(chat)).subscribe()
    }

    private fun startSubscribe() {
        subscribe = stompClient.join("/sub/message").subscribe {
            //message

            //kotlin은 NoArgsConstructor 미지원 -> dataclass에서 설정 필요..ㅅ
            val chat: Chat = mapper.readValue(it, Chat::class.java)
            if (chat.type == MessageType.REQUEST)
                musicUseCase.renewMusic()
            else if (chat.type == MessageType.UPDATE && UUID.fromString(chat.message)
                    .equals(user.uuid))
                userCase.renewUser(user.uuid) //유저 업데이트
            else if (chat.type == MessageType.USER || chat.type == MessageType.ALERT)
                chatLiveData.add(chat)
        }
    }

    fun close() {
        if (this::subscribe.isInitialized)
            subscribe.dispose()
        if (this::connection.isInitialized)
            connection.dispose()
    }

}