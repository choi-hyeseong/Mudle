package com.comet.mudle.repository.user.dao

import com.comet.mudle.web.rest.dto.UserRequestDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.ObjectResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface MudleUserAPI {

    @GET("/user")
    suspend fun getUser(@Query("uuid") uuid: UUID) : ApiResponse<ObjectResponse<UserResponseDTO>>

    @POST("/user")
    suspend fun register(@Body userDTO: UserRequestDTO) : ApiResponse<DefaultResponse>

}