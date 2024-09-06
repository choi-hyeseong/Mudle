package com.comet.mudle.repository.music

import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.dto.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.skydoves.sandwich.ApiResponse
import java.util.UUID

interface MusicRepository {

    suspend fun getMusic() : ApiResponse<ObjectResponse<MusicResponseDTO>>

    suspend fun requestMusic(uuid: UUID, music: String) : ApiResponse<DefaultResponse>

}