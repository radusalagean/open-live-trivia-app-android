package com.busytrack.openlivetrivia.application

import com.facebook.stetho.Stetho
import timber.log.Timber

class OpenLiveTriviaDebugApp : OpenLiveTriviaApp() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
    }
}