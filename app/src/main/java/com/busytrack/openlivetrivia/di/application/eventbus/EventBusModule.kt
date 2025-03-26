package com.busytrack.openlivetrivia.di.application.eventbus

import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.busytrack.openlivetrivia.generic.eventbus.EventBus
import dagger.Module
import dagger.Provides

@Module
class EventBusModule {

    @Provides
    @ApplicationScope
    fun provideEventBus(): EventBus = EventBus()
}