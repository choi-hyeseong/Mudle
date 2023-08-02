package com.comet.mudle.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.Music
import com.comet.mudle.model.ServerUser
import com.comet.mudle.repository.user.ServerUserRepository
import com.comet.mudle.repository.user.ServerUserRepositoryImpl
import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.web.rest.dto.MusicRequestDTO
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.comet.mudle.web.rest.response.DefaultResponse
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class MudleUserCase {
    //usecase
    private val serverUserRepository: ServerUserRepository = ServerUserRepositoryImpl()

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