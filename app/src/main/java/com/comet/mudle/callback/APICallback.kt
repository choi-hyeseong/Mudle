package com.comet.mudle.callback

import com.comet.mudle.model.Music
import java.util.UUID

interface APICallback {

    fun request(music : String)

    fun getMusic()

    fun getUser(uuid: UUID)

    fun setMusic(music: Music)
}