package com.busytrack.openlivetrivia.di.service

import android.app.Service
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ServiceModule(private val service: Service) {
    @Provides
    @ServiceScope
    fun provideService(): Service {
        return service
    }

    @Provides
    @ServiceScope
    fun provideContext(): Context {
        return service
    }
}