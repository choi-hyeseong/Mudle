package com.comet.mudle.module

import com.comet.mudle.preference.SharedPreferencesStorage
import com.comet.mudle.repository.music.MusicRepository
import com.comet.mudle.repository.music.ServerMusicRepository
import com.comet.mudle.repository.stomp.StompRepository
import com.comet.mudle.repository.user.LocalUUIDRepository
import com.comet.mudle.repository.user.PreferenceUUIDRepository
import com.comet.mudle.repository.user.UserRepository
import com.comet.mudle.repository.user.ServerUserRepository
import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.repository.user.dao.MudleUserAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    //bind - provide 차이 -> provide는 내가 직접 생성, bind는 @inject 사용
    @Provides
    @Singleton
    //qualifier로 다른 impl이랑 구분가능
    fun provideMusicRepository(musicAPI: MudleMusicAPI) : MusicRepository {
        return ServerMusicRepository(musicAPI)
    }

    @Provides
    @Singleton
    fun provideUUIDRepository(preferencesStorage: SharedPreferencesStorage) : LocalUUIDRepository {
        return PreferenceUUIDRepository(preferencesStorage)
    }

    @Provides
    @Singleton
    fun provideServerUserRepository(userAPI: MudleUserAPI) : UserRepository {
        return ServerUserRepository(userAPI)
    }

    @Provides
    @Singleton
    fun provideStompRepository( @NetworkModule.StompQualifier okHttpClient: OkHttpClient) : StompRepository {
        return StompRepository(okHttpClient)
    }

}