package com.comet.mudle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.User
import com.comet.mudle.repository.UserRepository
import com.comet.mudle.web.rest.MusicAPIManager
import java.util.UUID

class RegisterViewModel : ViewModel() {

    private val repository = UserRepository(DependencyUtil.preferences)
    private val musicAPIManager = MusicAPIManager()
    val validLiveData = MutableLiveData<String>()
    val responseLiveData = musicAPIManager.webResponse
    val registerLiveData = musicAPIManager.registerResponse

    fun register(name: String) {
        if (checkValidInput(name)) {
            val uuid = UUID.randomUUID()
            repository.saveUser(User(name, uuid))
            musicAPIManager.register(name, uuid)
        }
    }

    fun checkValidInput(name: String?): Boolean {
        if (name.isNullOrEmpty()) {
            validLiveData.value = "이름은 공백으로 지을 수 없습니다."
            return false
        }
        else if (name.equals("System", true)) {
            validLiveData.value = "해당 닉네임은 사용할 수 없습니다."
            return false
        }
        return true
    }

}