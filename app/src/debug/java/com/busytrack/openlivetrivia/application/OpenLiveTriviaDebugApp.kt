package com.busytrack.openlivetrivia.application

import com.busytrack.openlivetrivia.extension.isRobolectricUnitTest
import com.facebook.stetho.Stetho
import timber.log.Timber

class OpenLiveTriviaDebugApp : OpenLiveTriviaApp() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        if (!isRobolectricUnitTest()) {
            Stetho.initializeWithDefaults(this)
        }
    }
}