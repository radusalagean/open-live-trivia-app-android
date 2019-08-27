package com.busytrack.openlivetrivia.application

import android.os.Build
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

/**
 * Returns true if the current session is a Robolectric test one, false otherwise
 */
fun isRobolectricUnitTest() = "robolectric" == Build.FINGERPRINT