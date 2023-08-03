package com.comet.mudle.service

import androidx.lifecycle.LiveData
import com.comet.mudle.model.ServerUser
import com.comet.mudle.repository.user.ServerUserRepository
import com.comet.mudle.repository.user.ServerUserRepositoryImpl
import java.util.UUID

class ServerUserService(
    private val serverUserRepository: ServerUserRepository
) {
    //여러번 호출.
    fun getUser(uuid: UUID): LiveData<ServerUser> {
        return serverUserRepository.getUser(uuid)
    }

    //livedata를 return하게 해서 필요할때 사용할 수 있게.
    //서버 response를 livedata에 띄워주는것도 괜찮을듯?
    fun register(name: String, uuid: UUID): LiveData<Boolean> {
        return serverUserRepository.register(name, uuid)
    }

    fun getResponseLiveData(): LiveData<String> {
        return serverUserRepository.getResponseLiveData()
    }

    fun renewUser(uuid : UUID) {
        serverUserRepository.renewUser(uuid)
    }


}