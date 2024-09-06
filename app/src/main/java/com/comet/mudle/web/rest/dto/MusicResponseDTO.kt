package com.comet.mudle.web.rest.dto

import com.comet.mudle.model.Music

data class MusicResponseDTO(val link : String, val startTime : Long, val currentTime : Long, val isPlaying : Boolean) {

    fun toDomain() : Music = Music(link, startTime, currentTime, isPlaying)
}