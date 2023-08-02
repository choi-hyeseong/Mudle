package com.comet.mudle.repository.user

import android.content.SharedPreferences
import com.comet.mudle.repository.user.dao.PrefUserDao
import com.comet.mudle.model.LocalUser
import com.comet.mudle.repository.user.dao.UserDao

class LocalUserRepositoryImpl(private val userDao: UserDao) : LocalUserRepository {

    override fun getUser() : LocalUser {
        return userDao.loadUser()
    }

    override fun saveUser(localUser: LocalUser) {
        userDao.saveUser(localUser)
    }

    override fun existUser() : Boolean {
        return userDao.userExists()
    }
}