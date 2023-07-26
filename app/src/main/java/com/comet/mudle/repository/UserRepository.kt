package com.comet.mudle.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.dao.PrefUserDao
import com.comet.mudle.model.User

class UserRepository(preferences: SharedPreferences) {

    private val prefUserDao = PrefUserDao(preferences)

    fun getUser() : User {
        return prefUserDao.loadUser()
    }

    fun saveUser(user: User) {
        prefUserDao.saveUser(user)
    }

    fun existUser() : Boolean {
        return prefUserDao.userExists()
    }
}