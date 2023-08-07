package com.comet.mudle.module

import com.comet.mudle.repository.music.MusicRepository
import com.comet.mudle.repository.user.LocalUserRepository
import com.comet.mudle.repository.user.ServerUserRepository
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.MusicService
import com.comet.mudle.service.ServerUserService
import com.comet.mudle.web.stomp.StompService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//??????????????????? installin 라이프사이클인가 이거 서로 다르면 inject 안되는듯? 맞춰주니까 되네
//그니까 Singleton에서 fragment 접근할려고 해서 그런듯?
class ServiceModule {

    @Provides
    @Singleton
    fun provideLocalUserService(serverUserService: ServerUserService, localUserRepository: LocalUserRepository) : LocalUserService {
        return LocalUserService(serverUserService, localUserRepository)
    }

    @Provides
    @Singleton
    fun provideServerUserService(serverUserRepository: ServerUserRepository) : ServerUserService {
        return ServerUserService(serverUserRepository)
    }

    @Provides
    @Singleton
    fun provideMusicService(serverUserService: ServerUserService, musicRepository: MusicRepository) : MusicService {
        return MusicService(serverUserService, musicRepository)
    }

    @Provides
    @Singleton
    fun provideStompService(localUserService: LocalUserService, serverUserService: ServerUserService, musicService: MusicService, @NetworkModule.StompQualifier okHttpClient: OkHttpClient) : StompService {
        return StompService(localUserService, serverUserService, musicService, okHttpClient)
    }


}