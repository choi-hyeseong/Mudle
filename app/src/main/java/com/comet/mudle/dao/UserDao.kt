package com.comet.mudle.dao

import com.comet.mudle.model.User

interface UserDao {

    fun loadUser() : User

    fun saveUser(user : User)

    fun userExists() : Boolean
}