package com.comet.mudle.repository.stomp.callback

import com.comet.mudle.model.Chat

interface MessageCallback {

    fun onOpen()

    fun onClose()

    fun onMessage(chat : Chat)
}