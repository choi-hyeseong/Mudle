package com.comet.mudle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.repository.user.LocalUUIDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val uuidRepository: LocalUUIDRepository) : ViewModel() {

    // 유저 회원가입 정보 LiveData
    val userRegistrationLiveData : MutableLiveData<Boolean> by lazy {
        loadUserExists() // 유저 정보 로드 - 비동기
        MutableLiveData()
    }

    private fun loadUserExists() {
        CoroutineScope(Dispatchers.IO).launch {
            userRegistrationLiveData.postValue(uuidRepository.isUUIDExist())
        }

    }

}