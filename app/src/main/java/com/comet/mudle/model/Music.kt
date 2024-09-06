package com.comet.mudle.model

import com.comet.mudle.web.rest.dto.MusicResponseDTO

data class Music(val link : String, val startTime : Long, var currentTime : Long, val isPlaying : Boolean) {

    constructor(musicResponseDTO: MusicResponseDTO) : this(musicResponseDTO.link, musicResponseDTO.startTime, musicResponseDTO.currentTime, musicResponseDTO.isPlaying)

}