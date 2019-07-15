package com.busytrack.openlivetrivia.di.application.network

import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetriviainterface.rest.OpenLiveTriviaApiService
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {
    @Provides
    @ApplicationScope
    fun provideNetworkRepository(openLiveTriviaApiService: OpenLiveTriviaApiService) =
        NetworkRepository(openLiveTriviaApiService)
}