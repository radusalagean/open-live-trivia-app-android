package com.busytrack.openlivetrivia.di.application.scheduler

import com.busytrack.openlivetrivia.generic.scheduler.BaseSchedulerProvider
import com.busytrack.openlivetrivia.generic.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides

/**
 * Provides RxJava scheduler dependencies
 */
@Module
class SchedulerModule {

    @Provides
    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider()
}