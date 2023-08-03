package com.comet.mudle.module

import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.repository.user.dao.MudleUserAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val PREFERENCE = "USER"
const val URL: String = "http://192.168.219.106:8080"

@Module
@InstallIn(SingletonComponent::class) //application 종료시까지.
class NetworkModule {


    @Provides
    @Singleton
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create()).client(
                okHttpClient
            ).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun provideMusicAPI(retrofit: Retrofit) : MudleMusicAPI {
        return retrofit.create(MudleMusicAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserAPI(retrofit: Retrofit) : MudleUserAPI {
        return retrofit.create(MudleUserAPI::class.java)
    }
}