package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.model.LocalUser
import com.comet.mudle.model.Music
import com.comet.mudle.model.ServerUser
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.MusicService
import com.comet.mudle.service.ServerUserService
import com.comet.mudle.web.stomp.StompService

class GameViewModel : ViewModel() {

    private val serverUserService: ServerUserService = ServerUserService()
    private val musicService: MusicService = MusicService(serverUserService)
    private val localUserService : LocalUserService = LocalUserService(serverUserService)
    private val user: LocalUser = localUserService.getUser()
    val stompService: StompService =
        StompService(localUserService, musicService, serverUserService)
    var userLiveData: LiveData<ServerUser> = serverUserService.getUser(user.uuid)
    val musicLiveData: LiveData<Music> = musicService.getMusic()
    val userResponseLiveData: LiveData<String> = serverUserService.getResponseLiveData()
    val musicResponseLiveData : LiveData<String> = musicService.getResponseLiveData()

    fun request(url: String) {
        musicService.request(user.uuid, url)
    }

    fun renewMusic() {
        musicService.renewMusic()
    }

    override fun onCleared() {
        super.onCleared()
        stompService.close()
    }

}
