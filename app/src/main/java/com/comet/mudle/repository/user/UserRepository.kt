package com.comet.mudle.repository.user

import androidx.lifecycle.LiveData
import com.comet.mudle.model.User
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.ObjectResponse
import com.skydoves.sandwich.ApiResponse
import java.util.UUID

// 서버의 유저 정보 갱신
interface UserRepository {

    // responseDTO로 반환하긴 하지만 VM에서 User로 변경해서 다루기
    suspend fun getUser(uuid: UUID) : ApiResponse<ObjectResponse<UserResponseDTO>> //observe 가능하게

    suspend fun register(name : String, uuid: UUID) : ApiResponse<DefaultResponse>

}