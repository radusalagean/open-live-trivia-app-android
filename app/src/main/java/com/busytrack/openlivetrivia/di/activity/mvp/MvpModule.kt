package com.busytrack.openlivetrivia.di.activity.mvp

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationModel
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationMvp
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationPresenter
import dagger.Module
import dagger.Provides

@Module
class MvpModule {

    // Authentication Screen

    @Provides
    fun provideAuthenticationModel(networkRepository: NetworkRepository): AuthenticationMvp.Model =
        AuthenticationModel(networkRepository)

    @Provides
    fun provideAuthenticationPresenter(
        model: AuthenticationMvp.Model,
        authenticationManager: AuthenticationManager
    ): AuthenticationMvp.Presenter =
        AuthenticationPresenter(model, authenticationManager)
}