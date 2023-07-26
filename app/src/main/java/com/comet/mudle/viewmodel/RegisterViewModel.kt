package com.comet.mudle.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.User
import com.comet.mudle.repository.UserRepository
import java.util.UUID

class RegisterViewModel() : ViewModel() {

    private val repository = UserRepository(DependencyUtil.preferences)
    val liveData = MutableLiveData<String>()

    fun register(name: String): Boolean {
        if (checkValidInput(name)) {
            repository.saveUser(User(name, UUID.randomUUID()))
            return repository.existUser()
        }
        return false
    }

    private fun checkValidInput(name: String?): Boolean {
        if (name.isNullOrEmpty()) {
            liveData.value = "이름은 공백으로 지을 수 없습니다."
            return false
        }
        else if (name.equals("System", true)) {
            liveData.value = "해당 닉네임은 사용할 수 없습니다."
            return false
        }
        return true
    }

}