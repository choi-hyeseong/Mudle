package com.comet.mudle.web.rest

import com.comet.mudle.web.rest.dto.UserRequestDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.ObjectResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface MudleUserAPI {

    @GET("/user")
    fun user(@Query("uuid") uuid: UUID) : Call<ObjectResponse<UserResponseDTO>>

    @POST("/user")
    fun register(@Body userDTO: UserRequestDTO) : Call<DefaultResponse>

}