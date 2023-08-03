package com.comet.mudle.viewmodel

import androidx.lifecycle.ViewModel
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.ServerUserService

class MainViewModel : ViewModel() {

    private val localUserService : LocalUserService = LocalUserService(ServerUserService())

    fun isUserExists() : Boolean {
        return localUserService.isUserExists()
    }

}