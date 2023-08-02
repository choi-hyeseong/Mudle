package com.comet.mudle.web.rest.response

import com.comet.mudle.type.MessageType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

open class DefaultResponse @JsonCreator constructor(
    @JsonProperty("message") val message: String
    )