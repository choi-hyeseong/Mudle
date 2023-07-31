package com.comet.mudle.model

import com.comet.mudle.web.rest.dto.UserResponseDTO
import java.util.UUID

data class ServerUser(val name : String, val uuid: UUID, val coin : Int?) {

    constructor(userResponseDTO: UserResponseDTO) : this(userResponseDTO.name, userResponseDTO.uuid, userResponseDTO.coin)
}