package com.comet.mudle.repository.user

import com.comet.mudle.model.LocalUser

interface LocalUserRepository {

    fun getUser() : LocalUser

    fun saveUser(localUser: LocalUser)

    fun existUser() : Boolean
}