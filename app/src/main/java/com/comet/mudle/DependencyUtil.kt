package com.comet.mudle

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val PREFERENCE = "USER"
const val URL: String = "http://192.168.219.106:8080"

// Dagger2 와 같은 DI 대신 임시로 조치.
open class DependencyUtil {

    companion object {
        lateinit var preferences: SharedPreferences
        val retrofit = getRetrofitClient()

        fun injectPreference(context: Context) {
            preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        }

        private fun getRetrofitClient(): Retrofit {
            return Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create()).client(
                getOkHttp()
            ).build()
        }

        private fun getOkHttp(): OkHttpClient {
            return OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build()
        }
    }
}