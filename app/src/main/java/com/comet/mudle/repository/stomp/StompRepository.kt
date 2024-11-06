package com.comet.mudle.repository.stomp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.LOG
import com.comet.mudle.custom.ListLiveData
import com.comet.mudle.model.Chat
import com.comet.mudle.model.User
import com.comet.mudle.repository.stomp.callback.MessageCallback
import com.comet.mudle.type.MessageType
import com.fasterxml.jackson.databind.ObjectMapper
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.UUID

class StompRepository(private val okHttpClient: OkHttpClient) : NetworkRepository {

    private val URL = "ws://192.168.219.106:8080/ws"
    private val INTERVAL_MILLIS = 1000L

    private var connection: Disposable? = null
    private var subscribe: Disposable? = null
    private var stompClient: StompClient? = null
    private val mapper = ObjectMapper()

    override suspend fun connect(callback: MessageCallback) {
        stompClient = StompClient(okHttpClient, INTERVAL_MILLIS).also { it.url = URL }
        connectServer(stompClient!!, callback)
    }

    private fun connectServer(stompClient: StompClient, callback: MessageCallback) {
        connection = stompClient.connect().subscribe {
            //connection type - 얘도 구독중이기 때문에 event가 단발성으로 호출되는게 아님 - Close 되도 onClose 호출 가능!
            when (it.type) {
                Event.Type.OPENED -> {
                    callback.onOpen()
                    startSubscribe(stompClient, callback) //open시 재구독 -> 다시 접속해도 메시지 주고 받을 수 있게
                }
                else -> {
                    callback.onClose()
                    CoroutineScope(Dispatchers.IO).launch {
                        disconnect()
                    }
                }
            }
            Log.i(LOG, "${it.type}")
        }
    }

    override fun isConnected() = stompClient != null

    private fun startSubscribe(client : StompClient, callback: MessageCallback) {
        subscribe = client.join("/sub/message").subscribe {
            val chat: Chat = mapper.readValue(it, Chat::class.java)
            callback.onMessage(chat)
        }
    }

    override suspend fun disconnect() {
        subscribe?.dispose()
        connection?.dispose()
        stompClient = null
    }

    override suspend fun write(user : User, message: String) {
        if (stompClient == null)
            throw IllegalStateException("연결되지 않았습니다.")
        val chat = Chat(MessageType.USER, user.uuid, user.name, message, System.currentTimeMillis())
        stompClient!!.send("/pub/message", mapper.writeValueAsString(chat)).subscribe()
    }





}