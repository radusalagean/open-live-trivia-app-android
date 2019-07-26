package com.busytrack.openlivetrivia.di.activity.mvp

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationModel
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationMvp
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationPresenter
import com.busytrack.openlivetrivia.screen.game.GameModel
import com.busytrack.openlivetrivia.screen.game.GameMvp
import com.busytrack.openlivetrivia.screen.game.GamePresenter
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuModel
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuMvp
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuPresenter
import com.busytrack.openlivetriviainterface.socket.SocketHub
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

    // Game Screen

    @Provides
    fun provideGameModel(networkRepository: NetworkRepository): GameMvp.Model =
        GameModel(networkRepository)

    @Provides
    fun provideGamePresenter(
        model: GameMvp.Model,
        activityContract: ActivityContract,
        socketHub: SocketHub,
        authorizationManager: AuthorizationManager
    ): GameMvp.Presenter =
        GamePresenter(model, activityContract, socketHub, authorizationManager)
}