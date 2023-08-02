package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.usecase.MudleLocalUserCase
import com.comet.mudle.usecase.MudleUserCase

class RegisterViewModel : ViewModel() {

    private val mudleLocalUserCase : MudleLocalUserCase = MudleLocalUserCase(MudleUserCase())
    val responseLiveData : LiveData<String> = mudleLocalUserCase.responseLiveData
    val validLiveData : LiveData<String> = mudleLocalUserCase.validLiveData

    fun register(name: String) : LiveData<Boolean> {
        return mudleLocalUserCase.register(name)
    }


}