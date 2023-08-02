package com.comet.mudle.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.LocalUser
import com.comet.mudle.repository.user.LocalUserRepositoryImpl
import com.comet.mudle.repository.user.dao.PrefUserDao
import java.util.UUID

class MudleLocalUserCase(private val mudleUserCase: MudleUserCase) {

    private val repository = LocalUserRepositoryImpl(PrefUserDao(DependencyUtil.preferences))
    val validLiveData = MutableLiveData<String>()
    val responseLiveData : LiveData<String> = mudleUserCase.getResponseLiveData()

    fun register(name: String) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        if (checkValidInput(name)) {
            val uuid = UUID.randomUUID()
            repository.saveUser(LocalUser(name, uuid))
            return mudleUserCase.register(name, uuid)
        }
        return result
    }

    fun isUserExists(): Boolean {
        return repository.existUser()
    }

    fun getUser() : LocalUser {
        return repository.getUser()
    }

    private fun checkValidInput(name: String?): Boolean {
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