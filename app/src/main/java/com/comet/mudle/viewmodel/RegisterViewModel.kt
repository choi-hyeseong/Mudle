package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.ServerUserService

class RegisterViewModel : ViewModel() {

    private val localUserService : LocalUserService = LocalUserService(ServerUserService())
    val responseLiveData : LiveData<String> = localUserService.responseLiveData
    val validLiveData : LiveData<String> = localUserService.validLiveData

    fun register(name: String) : LiveData<Boolean> {
        return localUserService.register(name)
    }


}