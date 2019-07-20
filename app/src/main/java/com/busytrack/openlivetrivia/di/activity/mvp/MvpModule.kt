package com.busytrack.openlivetrivia.di.activity.mvp

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationModel
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationMvp
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationPresenter
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuModel
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuMvp
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuPresenter
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
        activityContract: ActivityContract,
        authenticationManager: AuthenticationManager
    ): AuthenticationMvp.Presenter =
        AuthenticationPresenter(model, activityContract, authenticationManager)

    // Main Menu Screen

    @Provides
    fun provideMainMenuModel(networkRepository: NetworkRepository): MainMenuMvp.Model =
        MainMenuModel(networkRepository)

    @Provides
    fun provideMainMenuPresenter(
        model: MainMenuMvp.Model
    ): MainMenuMvp.Presenter =
        MainMenuPresenter(model)
}