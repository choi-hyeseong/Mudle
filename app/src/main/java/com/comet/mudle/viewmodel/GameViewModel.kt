package com.comet.mudle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.custom.ListLiveData
import com.comet.mudle.model.Chat
import com.comet.mudle.type.MessageType
import com.fasterxml.jackson.databind.ObjectMapper
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import java.util.UUID

class GameViewModel : ViewModel() {

    val chatList : ListLiveData<Chat> = ListLiveData()
    lateinit var connection : Disposable
    lateinit var topic : Disposable
    lateinit var stompClient: StompClient
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
                Log.i("asd", "${it.type}")
            }

            topic = stompClient.join("/sub/message").subscribe {
                //message
                val mapper = ObjectMapper()
                //kotlin은 NoArgsConstructor 미지원 -> dataclass에서 설정 필요..ㅅ
                val chat : Chat = mapper.readValue(it, Chat::class.java)
                chatList.add(chat)
            }

        }.start()
    }

    fun send(message : String) {
        //subscribe 까지
        val mapper = ObjectMapper()
        val chat = Chat(MessageType.USER, UUID.randomUUID(), "test", message)
        stompClient.send("/pub/message", mapper.writeValueAsString(chat)).subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        connection.dispose()
        topic.dispose()
    }



}