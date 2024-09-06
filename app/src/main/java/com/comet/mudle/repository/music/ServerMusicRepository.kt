package com.comet.mudle.repository.music

import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.web.rest.dto.MusicRequestDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.dto.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.skydoves.sandwich.ApiResponse
import java.util.UUID

class ServerMusicRepository(
    private val musicAPI: MudleMusicAPI
) : MusicRepository {

    override suspend fun getMusic(): ApiResponse<ObjectResponse<MusicResponseDTO>> {
        return musicAPI.getMusic()
    }

    override suspend fun requestMusic(uuid: UUID, music: String): ApiResponse<DefaultResponse> {
        return musicAPI.requestMusic(MusicRequestDTO(uuid, music))
    }
}
