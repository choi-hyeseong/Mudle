package com.comet.mudle.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.model.LocalUser
import com.comet.mudle.model.Music
import com.comet.mudle.model.ServerUser
import com.comet.mudle.usecase.MudleLocalUserCase
import com.comet.mudle.usecase.MudleMusicCase
import com.comet.mudle.usecase.MudleUserCase
import com.comet.mudle.web.stomp.StompUseCase

class GameViewModel : ViewModel() {

    private val mudleUserCase: MudleUserCase = MudleUserCase()
    private val mudleMusicCase: MudleMusicCase = MudleMusicCase(mudleUserCase)
    private val mudleLocalUserCase : MudleLocalUserCase = MudleLocalUserCase(mudleUserCase)
    private val user: LocalUser = mudleLocalUserCase.getUser()
    val stompUseCase: StompUseCase =
        StompUseCase(mudleLocalUserCase, mudleMusicCase, mudleUserCase)
    var userLiveData: LiveData<ServerUser> = mudleUserCase.getUser(user.uuid)
    val musicLiveData: LiveData<Music> = mudleMusicCase.getMusic()
    val userResponseLiveData: LiveData<String> = mudleUserCase.getResponseLiveData()
    val musicResponseLiveData : LiveData<String> = mudleMusicCase.getResponseLiveData()

    fun request(url: String) {
        mudleMusicCase.request(user.uuid, url)
    }

    fun renewMusic() {
        mudleMusicCase.renewMusic()
    }

    override fun onCleared() {
        super.onCleared()
        stompUseCase.close()
    }

}
