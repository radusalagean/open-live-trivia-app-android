package com.busytrack.openlivetrivia.di.application.socket

import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.busytrack.openlivetriviainterface.socket.SocketHub
import dagger.Module
import dagger.Provides

@Module
class SocketModule {
    @Provides
    @ApplicationScope
    fun provideSocketHub(): SocketHub = SocketHub()
}