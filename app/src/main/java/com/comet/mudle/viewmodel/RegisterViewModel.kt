package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.LocalUser
import com.comet.mudle.repository.UserRepository
import com.comet.mudle.web.rest.MudleAPIManager
import java.util.UUID

class RegisterViewModel : ViewModel() {

    private val repository = UserRepository(DependencyUtil.preferences)
    private val mudleAPIManager = MudleAPIManager()
    val validLiveData = MutableLiveData<String>()
    val responseLiveData = mudleAPIManager.webResponse

    fun register(name: String) : LiveData<Boolean>? {
        if (checkValidInput(name)) {
            val uuid = UUID.randomUUID()
            repository.saveUser(LocalUser(name, uuid))
            return mudleAPIManager.register(name, uuid)
        }
        return null
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