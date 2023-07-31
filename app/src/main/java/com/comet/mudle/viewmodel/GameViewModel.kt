package com.comet.mudle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.Music
import com.comet.mudle.model.ServerUser
import com.comet.mudle.repository.UserRepository
import com.comet.mudle.web.rest.MudleAPIManager
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.stomp.StompManager

class GameViewModel : ViewModel() {

    private val mudleAPIManager: MudleAPIManager = MudleAPIManager()
    val stompManager: StompManager =
        StompManager(UserRepository(DependencyUtil.preferences), mudleAPIManager)
    val userLiveData: MutableLiveData<ServerUser> = mudleAPIManager.userLiveData
    val musicLiveData: MutableLiveData<Music> = mudleAPIManager.musicLiveData
    val responseLiveData: MutableLiveData<String> = mudleAPIManager.webResponse

    fun request(music: String) {
        mudleAPIManager.request(music)
    }

    override fun onCleared() {
        super.onCleared()
        stompManager.close()
    }

}
