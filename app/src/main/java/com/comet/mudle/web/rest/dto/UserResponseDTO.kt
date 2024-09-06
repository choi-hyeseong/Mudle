package com.comet.mudle.web.rest.dto

import com.comet.mudle.model.User
import java.util.UUID

data class UserResponseDTO(val name : String, val uuid: UUID, val coin : Int) {

    fun toDomain() : User = User(name, uuid, coin)
}