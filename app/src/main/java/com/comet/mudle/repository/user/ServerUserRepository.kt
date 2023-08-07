package com.comet.mudle.repository.user

import androidx.lifecycle.LiveData
import com.comet.mudle.model.ServerUser
import com.comet.mudle.repository.ResponseLiveDataHolder
import java.util.UUID

interface ServerUserRepository : ResponseLiveDataHolder{

    fun getUser(uuid: UUID) : LiveData<ServerUser> //observe 가능하게

    fun register(name : String, uuid: UUID) : LiveData<Boolean>

    //기존 LiveData의 갱신을 요청
    suspend fun renewUser(uuid: UUID)

}