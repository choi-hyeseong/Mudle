package com.comet.mudle.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.LOG
import com.comet.mudle.model.User

import com.comet.mudle.repository.user.dao.MudleUserAPI
import com.comet.mudle.web.rest.dto.UserRequestDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.ObjectResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ServerUserRepository(private val userAPI: MudleUserAPI) : UserRepository {

    override suspend fun getUser(uuid: UUID): ApiResponse<ObjectResponse<UserResponseDTO>> {
        return userAPI.getUser(uuid)
    }
    
    override suspend fun register(name: String, uuid: UUID): ApiResponse<DefaultResponse> {
        return userAPI.register(UserRequestDTO(name, uuid))
    }
}
