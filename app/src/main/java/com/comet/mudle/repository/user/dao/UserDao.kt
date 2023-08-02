package com.comet.mudle.repository.user.dao

import com.comet.mudle.model.LocalUser

interface UserDao {

    fun loadUser() : LocalUser

    fun saveUser(localUser : LocalUser)

    fun userExists() : Boolean
}