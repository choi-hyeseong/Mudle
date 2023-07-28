package com.comet.mudle.model

import com.comet.mudle.web.rest.response.MusicResponseDTO

data class Music(val link : String, val startTime : Long, val isPlaying : Boolean) {

    constructor(musicResponseDTO: MusicResponseDTO) : this(musicResponseDTO.link, musicResponseDTO.startTime, musicResponseDTO.isPlaying)

}