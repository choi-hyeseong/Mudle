package com.comet.mudle.repository.user.dao

import com.comet.mudle.web.rest.dto.MusicRequestDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MudleMusicAPI {

    //music={music}이렇게 안해도 됨
    @POST("/request")
    suspend fun requestMusic(@Body musicRequestDTO: MusicRequestDTO) : ApiResponse<DefaultResponse>

    @GET("/music")
    suspend fun getMusic() : ApiResponse<ObjectResponse<MusicResponseDTO>>

}