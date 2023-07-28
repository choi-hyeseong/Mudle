package com.comet.mudle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.Music
import com.comet.mudle.repository.UserRepository
import com.comet.mudle.web.rest.MusicAPIManager
import com.comet.mudle.web.stomp.StompManager

class GameViewModel : ViewModel() {

    private val musicAPIManager : MusicAPIManager = MusicAPIManager(this)
    private val responseLiveData = MutableLiveData<String>()
    val musicLiveData: MutableLiveData<Music> = MutableLiveData()
    val stompManager : StompManager = StompManager(UserRepository(DependencyUtil.preferences), this)

    fun request(music : String) {
        musicAPIManager.request(music)
    }

    fun getMusic() {
        musicAPIManager.getMusic()
    }

    override fun onCleared() {
        super.onCleared()
        stompManager.close()
    }

}
