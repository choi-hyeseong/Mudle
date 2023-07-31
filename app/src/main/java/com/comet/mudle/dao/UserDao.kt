package com.comet.mudle.dao

import com.comet.mudle.model.LocalUser

interface UserDao {

    fun loadUser() : LocalUser

    fun saveUser(localUser : LocalUser)

    fun userExists() : Boolean
}