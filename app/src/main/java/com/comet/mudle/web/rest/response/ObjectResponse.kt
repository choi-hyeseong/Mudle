package com.comet.mudle.web.rest.response

class ObjectResponse<T>(message : String, val content : T) : DefaultResponse(message)