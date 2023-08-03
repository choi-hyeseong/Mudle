package com.comet.mudle.module

import com.comet.mudle.repository.music.MusicRepository
import com.comet.mudle.repository.music.MusicRepositoryImpl
import com.comet.mudle.repository.user.LocalUserRepository
import com.comet.mudle.repository.user.LocalUserRepositoryImpl
import com.comet.mudle.repository.user.ServerUserRepository
import com.comet.mudle.repository.user.ServerUserRepositoryImpl
import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.repository.user.dao.MudleUserAPI
import com.comet.mudle.repository.user.dao.UserDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    //bind - provide 차이 -> provide는 내가 직접 생성, bind는 @inject 사용
    @Provides
    @Singleton
    //qualifier로 다른 impl이랑 구분가능
    fun provideMusicRepository(musicAPI: MudleMusicAPI) : MusicRepository {
        return MusicRepositoryImpl(musicAPI)
    }

    @Provides
    @Singleton
    fun provideLocalUserRepository(userDao: UserDao) : LocalUserRepository {
        return LocalUserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideServerUserRepository(userAPI: MudleUserAPI) : ServerUserRepository {
        return ServerUserRepositoryImpl(userAPI)
    }
}