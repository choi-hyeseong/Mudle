package com.comet.mudle.web.rest

import com.comet.mudle.web.rest.dto.MusicRequestDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MudleAPI {

    //music={music}이렇게 안해도 됨
    @POST("/request")
    fun requestMusic(@Body musicRequestDTO: MusicRequestDTO) : Call<DefaultResponse>

    @GET("/music")
    fun getMusic() : Call<ObjectResponse<MusicResponseDTO>>

}