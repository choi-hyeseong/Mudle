package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.ServerUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val localUserService : LocalUserService
) : ViewModel() {


    val responseLiveData : LiveData<String> = localUserService.getResponseLiveData()
    val validLiveData : LiveData<String> = localUserService.validLiveData

    fun register(name: String) : LiveData<Boolean> {
        return localUserService.register(name)
    }


}