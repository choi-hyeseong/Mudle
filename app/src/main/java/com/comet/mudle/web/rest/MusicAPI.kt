package com.comet.mudle.web.rest

import com.comet.mudle.web.rest.dto.UserDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.comet.mudle.web.rest.response.DefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface MusicAPI {

    //music={music}이렇게 안해도 됨
    @GET("/request")
    fun requestMusic(@Query("music") music : String) : Call<Void>

    @GET("/user")
    fun user(@Query("uuid") uuid: UUID) : Call<ObjectResponse<UserResponseDTO>>

    @POST("/user")
    fun register(@Body userDTO: UserDTO) : Call<DefaultResponse>

    @GET("/music")
    fun getMusic() : Call<ObjectResponse<MusicResponseDTO>>

}