package com.busytrack.openlivetrivia.application

import android.app.Application
import com.busytrack.openlivetrivia.di.application.ApplicationComponent
import com.busytrack.openlivetrivia.di.application.ApplicationModule
import com.busytrack.openlivetrivia.di.application.DaggerApplicationComponent

open class OpenLiveTriviaApp : Application() {
    val applicationComponent: ApplicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
}