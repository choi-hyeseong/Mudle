package com.comet.mudle.repository.stomp

import com.comet.mudle.model.User
import com.comet.mudle.repository.stomp.callback.MessageCallback

interface NetworkRepository {

    /**
     * 이미 연결된경우 IS throw
     */
    suspend fun connect(callback : MessageCallback)

    /**
     * 연결되지 않은경우 IS throw
     */
    suspend fun disconnect()

    /**
     * 연결되지 않은경우 IS Throw
     */
    suspend fun write(user : User, message : String)

    fun isConnected() : Boolean
}