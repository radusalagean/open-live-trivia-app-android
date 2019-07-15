package com.busytrack.openlivetrivia.di.application

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @ApplicationScope
    fun provideApplication(): Application = application
}