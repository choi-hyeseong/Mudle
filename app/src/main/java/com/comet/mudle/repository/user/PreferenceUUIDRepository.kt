package com.comet.mudle.repository.user

import android.util.Log
import com.comet.mudle.preference.SharedPreferencesStorage
import java.util.UUID

// SP사용하는 Preference User 정보 저장소
class PreferenceUUIDRepository(private val sharedPreferencesStorage: SharedPreferencesStorage) : LocalUUIDRepository {

    private val UUID_KEY = "USER_UUID"

    // SP Load
    override suspend fun getUUID(): UUID? {
        return kotlin.runCatching {
            sharedPreferencesStorage.loadObject(UUID_KEY, UUID::class)
        }.onFailure {
            Log.w(this::class.simpleName, it.message ?: "error")
        }.getOrNull()
    }

    override suspend fun saveUUID(uuid: UUID) {
        sharedPreferencesStorage.saveObject(UUID_KEY, uuid)
    }

    override suspend fun isUUIDExist(): Boolean {
        return sharedPreferencesStorage.isContains(UUID_KEY)
    }


}