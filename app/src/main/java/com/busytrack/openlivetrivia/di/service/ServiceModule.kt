package com.busytrack.openlivetrivia.di.service

import android.app.Service
import android.content.Context
import com.busytrack.openlivetrivia.di.NAMED_SERVICE_CONTEXT
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ServiceModule(private val service: Service) {
    @Provides
    @ServiceScope
    fun provideService(): Service {
        return service
    }

    @Provides
    @Named(NAMED_SERVICE_CONTEXT)
    @ServiceScope
    fun provideContext(): Context {
        return service
    }
}