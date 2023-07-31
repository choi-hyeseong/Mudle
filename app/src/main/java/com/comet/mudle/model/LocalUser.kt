package com.comet.mudle.model

import com.comet.mudle.web.rest.dto.UserRequestDTO
import java.util.UUID

data class LocalUser(val name : String, val uuid: UUID, val coin : Int?) {

    constructor(name : String, uuid: UUID) : this(name, uuid, 0)

    fun convertDTO() : UserRequestDTO {
        return UserRequestDTO(name, uuid)
    }
}