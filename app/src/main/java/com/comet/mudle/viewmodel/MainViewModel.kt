package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.ServerUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val localUserService: LocalUserService) : ViewModel() {

    fun isUserExists() : LiveData<Boolean> {
        return MutableLiveData(localUserService.isUserExists())
    }

}