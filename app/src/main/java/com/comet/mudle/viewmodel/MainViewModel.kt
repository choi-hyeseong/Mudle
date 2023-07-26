package com.comet.mudle.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.repository.UserRepository

class MainViewModel : ViewModel() {

    private val repository = UserRepository(DependencyUtil.preferences)

    fun isUserExists() : Boolean {
        return repository.existUser()
    }

}