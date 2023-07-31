package com.comet.mudle.repository

import android.content.SharedPreferences
import com.comet.mudle.dao.PrefUserDao
import com.comet.mudle.model.LocalUser

class UserRepository(preferences: SharedPreferences) {

    private val prefUserDao = PrefUserDao(preferences)

    fun getUser() : LocalUser {
        return prefUserDao.loadUser()
    }

    fun saveUser(localUser: LocalUser) {
        prefUserDao.saveUser(localUser)
    }

    fun existUser() : Boolean {
        return prefUserDao.userExists()
    }
}