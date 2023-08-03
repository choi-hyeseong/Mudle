package com.comet.mudle

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MudleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}