package com.comet.mudle.viewmodel

import androidx.lifecycle.ViewModel
import com.comet.mudle.usecase.MudleLocalUserCase
import com.comet.mudle.usecase.MudleUserCase

class MainViewModel : ViewModel() {

    private val mudleLocalUserCase : MudleLocalUserCase = MudleLocalUserCase(MudleUserCase())

    fun isUserExists() : Boolean {
        return mudleLocalUserCase.isUserExists()
    }

}