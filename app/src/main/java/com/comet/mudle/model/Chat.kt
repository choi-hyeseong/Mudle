package com.comet.mudle.model

import com.comet.mudle.type.MessageType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class Chat @JsonCreator constructor(
    @JsonProperty("type") val type: MessageType,
    @JsonProperty("uuid") val uuid: UUID,
    @JsonProperty("sender") val sender: String,
    @JsonProperty("message") val message: String
)