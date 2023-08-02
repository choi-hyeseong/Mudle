package com.comet.mudle.repository.user.dao

import android.content.SharedPreferences
import com.comet.mudle.model.LocalUser
import java.util.UUID

class PrefUserDao(private val preferences: SharedPreferences) : UserDao {

    //Exist 체크 후 요청바람.
    override fun loadUser(): LocalUser {
        val name : String = preferences.getString("name", "null")!!
        val uuid : UUID = preferences.getString("uuid", UUID.randomUUID().toString())?.run {
            UUID.fromString(this)
        }!!
        return LocalUser(name, uuid)
    }

    override fun saveUser(localUser: LocalUser) {
        preferences.edit().putString("name", localUser.name).putString("uuid", localUser.uuid.toString()).commit()
    }

    override fun userExists(): Boolean {
        return preferences.contains("name") && preferences.contains("uuid")
    }
}