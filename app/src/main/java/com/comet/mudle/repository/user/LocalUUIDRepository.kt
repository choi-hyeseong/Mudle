package com.comet.mudle.repository.user

import java.util.UUID

interface LocalUUIDRepository {

    // 로컬에 저장된 고유 uuid를 가져옵니다. 저장되어 있지 않을경우 null
    suspend fun getUUID() : UUID?

    // uuid 저장
    suspend fun saveUUID(uuid: UUID)

    // 로컬에 uuid가 저장되어 있는지 확인
    suspend fun isUUIDExist() : Boolean
}