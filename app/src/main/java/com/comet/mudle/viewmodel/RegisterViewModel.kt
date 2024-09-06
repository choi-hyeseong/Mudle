package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.repository.user.LocalUUIDRepository
import com.comet.mudle.repository.user.ServerUserRepository
import com.comet.mudle.repository.user.UserRepository
import com.comet.mudle.web.rest.response.DefaultResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val uuidRepository: LocalUUIDRepository, private val userRepository: UserRepository) : ViewModel() {

    val validLiveData : MutableLiveData<String> = MutableLiveData()
    val responseLiveData : MutableLiveData<Boolean> = MutableLiveData()

    fun register(name: String?) {
        val uuid : UUID = UUID.randomUUID()
        if (!checkValidInput(name))
            return
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.register(name!!, uuid).suspendOnSuccess {
                uuidRepository.saveUUID(uuid)
                responseLiveData.postValue(true)
            }.onFailure {
                responseLiveData.postValue(false)
            }.onError {
                responseLiveData.postValue(false)
            }
        }
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