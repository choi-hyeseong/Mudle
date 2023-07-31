package com.comet.mudle.web.rest.dto

import java.util.UUID

data class UserResponseDTO(val name : String, val uuid: UUID, val coin : Int)