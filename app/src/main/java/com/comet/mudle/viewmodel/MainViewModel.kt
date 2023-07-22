package com.comet.mudle.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.repository.UserRepository

class MainViewModel(private val preferences: SharedPreferences) : ViewModel() {

    private val repository = UserRepository(preferences)

    fun isUserExists() : Boolean {
        return repository.existUser()
    }

}