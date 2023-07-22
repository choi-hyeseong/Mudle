package com.comet.mudle.dao

import android.content.SharedPreferences
import com.comet.mudle.model.User
import java.util.UUID

class PrefUserDao(private val preferences: SharedPreferences) : UserDao {

    //Exist 체크 후 요청바람.
    override fun loadUser(): User {
        val name : String = preferences.getString("name", "null")!!
        val uuid : UUID = preferences.getString("uuid", UUID.randomUUID().toString())?.run {
            UUID.fromString(this)
        }!!
        return User(name, uuid)
    }

    override fun saveUser(user: User) {
        preferences.edit().putString("name", user.name).putString("uuid", user.uuid.toString()).commit()
    }

    override fun userExists(): Boolean {
        return preferences.contains("name") && preferences.contains("uuid")
    }
}