package com.comet.mudle.module

import android.content.Context
import android.content.SharedPreferences
import com.comet.mudle.preference.SharedPreferencesStorage
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
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferencesStorage {
        return SharedPreferencesStorage(context)
    }
}