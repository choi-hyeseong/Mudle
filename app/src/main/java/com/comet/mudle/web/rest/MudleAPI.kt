package com.comet.mudle.web.rest

import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MudleAPI {

    //music={music}이렇게 안해도 됨
    @GET("/request")
    fun requestMusic(@Query("music") music : String) : Call<Void>

    @GET("/music")
    fun getMusic() : Call<ObjectResponse<MusicResponseDTO>>

}