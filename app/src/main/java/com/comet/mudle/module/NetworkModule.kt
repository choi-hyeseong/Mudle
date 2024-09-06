package com.comet.mudle.module

import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.repository.user.dao.MudleUserAPI
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

const val URL: String = "http://192.168.219.106:8080"

@Module
@InstallIn(SingletonComponent::class) //application 종료시까지.
class NetworkModule {

    //모듈에 annotation을 넣어야 되는구나..
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RestQualifier

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class StompQualifier


    @Provides
    @Singleton
    fun provideRetrofitClient(@RestQualifier okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create()).client(
                okHttpClient
            )
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @RestQualifier
    fun provideRESTOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build()
        //아이고.. 다른 서비스에서 get요청 하던거 response자너..
    }

    @Provides
    @Singleton
    @StompQualifier
    fun provideSTOMPOkHttpClient() : OkHttpClient {
        //얘는 timeout이 뜰때마다 reconnect를 시도하게 구성한거라, 짧게 잡아야함ㅁㄴ
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