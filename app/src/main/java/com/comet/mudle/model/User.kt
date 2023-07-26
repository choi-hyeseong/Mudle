package com.comet.mudle.model

import com.comet.mudle.web.rest.dto.UserDTO
import java.util.UUID

data class User(val name : String, val uuid: UUID) {

    fun convertDTO() : UserDTO {
        return UserDTO(name, uuid)
    }
}