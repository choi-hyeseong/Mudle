package com.comet.mudle.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.LocalUser
import com.comet.mudle.repository.ResponseLiveDataHolder
import com.comet.mudle.repository.user.LocalUserRepository
import com.comet.mudle.repository.user.LocalUserRepositoryImpl
import com.comet.mudle.repository.user.dao.PrefUserDao
import java.util.UUID

class LocalUserService(
    private val serverUserService: ServerUserService,
    private val repository: LocalUserRepository,
) : ResponseLiveDataHolder {

    val validLiveData = MutableLiveData<String>()

    fun register(name: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        if (checkValidInput(name)) {
            val uuid = UUID.randomUUID()
            repository.saveUser(LocalUser(name, uuid))
            return serverUserService.register(name, uuid)
        }
        return result
    }

    fun isUserExists(): Boolean {
        return repository.existUser()
    }

    fun getUser(): LocalUser {
        return repository.getUser()
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

    override fun getResponseLiveData(): LiveData<String> {
        return serverUserService.getResponseLiveData() //생성자 초기화때가 아닌 method로 가져올 수 있게
    }


}