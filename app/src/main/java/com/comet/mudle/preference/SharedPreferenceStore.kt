package com.comet.mudle.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.reflect.KClass

/**
 * Dao용 SP 데이터 저장소
 */
const val PREFERENCE_KEY = "MUDLE_PREFERENCE" //preference 파일 키값. 고정

class SharedPreferencesStorage(context : Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        //private 모드로 PREFERENCE_KEY를 가진 preference 접근
        context.getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE)
    }
    private val gson : Gson = Gson()

    suspend fun saveObject(key: String, value: Any): Boolean {
        return sharedPreferences.edit().putString(key, gson.toJson(value)).commit()
    }

    suspend fun <T : Any> loadObject(key: String, targetClass: KClass<T>): T {
        //key값이 저장되어 있지 않거나, String형식이 아닌경우 Exception
        if (!isContains(key) || sharedPreferences.getString(key, null) == null) throw IllegalArgumentException("해당 key값에 저장된 값이 없습니다.")
        try {
            //getString이 notNull임은 위 if문에서 보증
            return gson.fromJson(sharedPreferences.getString(key, null), targetClass.java)
        }
        catch (e: Exception) {
            throw IllegalArgumentException("해당 객체로 역직렬화 할 수 없습니다. ${targetClass.simpleName} | ${e.message}")
        }
    }

    suspend fun delete(key: String): Boolean {
        return sharedPreferences.edit().remove(key).commit()
    }

    suspend fun putInt(key: String, value: Int): Boolean {
        return sharedPreferences.edit().putInt(key, value).commit()
    }

    suspend fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    suspend fun putDouble(key: String, value: Double): Boolean {
        return sharedPreferences.edit().putFloat(key, value.toFloat()).commit()
    }

    suspend fun getDouble(key: String, defaultValue: Double): Double {
        return sharedPreferences.getFloat(key, defaultValue.toFloat()).toDouble()
    }

    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    suspend fun putBoolean(key: String, value: Boolean): Boolean {
        return sharedPreferences.edit().putBoolean(key, value).commit()
    }

    suspend fun isContains(key : String) : Boolean {
        return sharedPreferences.contains(key)
    }
}