package com.comet.mudle.repository.music

import androidx.lifecycle.LiveData
import com.comet.mudle.model.Music
import com.comet.mudle.repository.ResponseLiveDataHolder
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface MusicRepository : ResponseLiveDataHolder {

    fun getMusic() : LiveData<Music>

    suspend fun requestMusic(uuid: UUID, music: String)

    suspend fun renewMusic()

}