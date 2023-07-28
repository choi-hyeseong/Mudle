package com.comet.mudle.web.rest

import com.comet.mudle.web.rest.dto.UserDTO
import com.comet.mudle.web.rest.response.MusicResponse
import com.comet.mudle.web.rest.response.MusicResponseDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MusicAPI {

    //music={music}이렇게 안해도 됨
    @GET("/request")
    fun requestMusic(@Query("music") music : String) : Call<Void>

    //TODO
    @POST("/register")
    fun register(@Body userDTO: UserDTO) : Call<Void>

    @GET("/music")
    fun getMusic() : Call<MusicResponse>
}