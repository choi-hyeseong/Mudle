package com.comet.mudle.web.stomp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.LOG
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.UUID

class StompService(private val localUserService: LocalUserService,
                   private val serverUserService: ServerUserService,
                   private val musicService: MusicService,
                   private val okHttpClient: OkHttpClient
) {

    private lateinit var connection: Disposable
    private lateinit var subscribe: Disposable
    private lateinit var stompClient: StompClient
    private val user = localUserService.getUser()
    private val mapper = ObjectMapper()
    val chatLiveData: ListLiveData<Chat> = ListLiveData()
    val serverStatLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun connect() {
        //okhttp내 websocket 사용 -> 코루틴 / Thread 없이도 자체 쓰레드로 돌아감
        val url = "ws://192.168.219.106:8080/ws"
        val intervalMillis = 1000L
        stompClient = StompClient(okHttpClient, intervalMillis).let {
            it.url = url
            it
        }
        connection = stompClient.connect().subscribe {
            //connection type
            when (it.type) {
                Event.Type.OPENED -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        serverUserService.renewUser(user.uuid)
                        musicService.renewMusic()
                    }
                    serverStatLiveData.postValue(true)
                    startSubscribe() //open시 재구독 -> 다시 접속해도 메시지 주고 받을 수 있게
                }

                else -> {
                    serverStatLiveData.postValue(false)
                    if (this@StompService::subscribe.isInitialized) //this@Class 순으로 반대로 써야댐..
                        subscribe.dispose() //구독만 취소 (connection 끊으면 reconnect 안함)
                }
            }
            Log.i(LOG, "${it.type}")
        }

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
                CoroutineScope(Dispatchers.IO).launch { musicService.renewMusic() }
            else if (chat.type == MessageType.UPDATE && UUID.fromString(chat.message)
                    .equals(user.uuid))
                CoroutineScope(Dispatchers.IO).launch { serverUserService.renewUser(user.uuid) }
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