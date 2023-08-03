package com.comet.mudle.module

import android.content.Context
import android.content.SharedPreferences
import com.comet.mudle.repository.user.dao.PrefUserDao
import com.comet.mudle.repository.user.dao.UserDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideUserDao(preferences: SharedPreferences) : UserDao {
        return PrefUserDao(preferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(com.comet.mudle.PREFERENCE, Context.MODE_PRIVATE)
    }
}