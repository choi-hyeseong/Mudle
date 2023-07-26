package com.comet.mudle

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyUtil.injectPreference(this)
    }
}