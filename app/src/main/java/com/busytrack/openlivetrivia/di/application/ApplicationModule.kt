package com.busytrack.openlivetrivia.di.application

import android.app.Application
import android.content.Context
import com.busytrack.openlivetrivia.di.NAMED_APPLICATION_CONTEXT
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @ApplicationScope
    fun provideApplication(): Application = application

    @Provides
    @Named(NAMED_APPLICATION_CONTEXT)
    @ApplicationScope
    fun provideContext(): Context = application
}