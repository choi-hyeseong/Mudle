package com.comet.mudle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.callback.APICallback
import com.comet.mudle.model.Music
import com.comet.mudle.repository.UserRepository
import com.comet.mudle.web.rest.MusicAPIManager
import com.comet.mudle.web.stomp.StompManager
import java.util.UUID

class GameViewModel : ViewModel(), APICallback {

    private val musicAPIManager : MusicAPIManager = MusicAPIManager()
    private val responseLiveData = musicAPIManager.webResponse
    val musicLiveData: MutableLiveData<Music> = musicAPIManager.musicLiveData
    val stompManager : StompManager = StompManager(UserRepository(DependencyUtil.preferences), this)
    val userLiveData = musicAPIManager.userLiveData

    override fun request(music : String) {
        musicAPIManager.request(music)
    }

    override fun getMusic() {
        musicAPIManager.getMusic()
    }

    override fun getUser(uuid: UUID) {
        musicAPIManager.getUser(uuid)
    }

    override fun setMusic(music: Music) {
        musicLiveData.postValue(music)
    }

    override fun onCleared() {
        super.onCleared()
        stompManager.close()
    }

}
