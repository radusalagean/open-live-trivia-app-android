package com.busytrack.openlivetrivia.di.activity.mvp

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetrivia.persistence.database.DatabaseRepository
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationModel
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationMvp
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationPresenter
import com.busytrack.openlivetrivia.screen.game.GameModel
import com.busytrack.openlivetrivia.screen.game.GameMvp
import com.busytrack.openlivetrivia.screen.game.GamePresenter
import com.busytrack.openlivetrivia.screen.leaderboard.LeaderboardModel
import com.busytrack.openlivetrivia.screen.leaderboard.LeaderboardMvp
import com.busytrack.openlivetrivia.screen.leaderboard.LeaderboardPresenter
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuModel
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuMvp
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuPresenter
import com.busytrack.openlivetrivia.screen.moderatereports.ModerateReportsModel
import com.busytrack.openlivetrivia.screen.moderatereports.ModerateReportsMvp
import com.busytrack.openlivetrivia.screen.moderatereports.ModerateReportsPresenter
import com.busytrack.openlivetrivia.screen.settings.SettingsModel
import com.busytrack.openlivetrivia.screen.settings.SettingsMvp
import com.busytrack.openlivetrivia.screen.settings.SettingsPresenter
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
        authenticationManager: AuthenticationManager,
        authorizationManager: AuthorizationManager,
        dialogManager: DialogManager
    ): AuthenticationMvp.Presenter =
        AuthenticationPresenter(model, activityContract, authenticationManager, authorizationManager, dialogManager)

    // Main Menu Screen

    @Provides
    fun provideMainMenuModel(networkRepository: NetworkRepository): MainMenuMvp.Model =
        MainMenuModel(networkRepository)

    @Provides
    fun provideMainMenuPresenter(
        model: MainMenuMvp.Model,
        activityContract: ActivityContract,
        authenticationManager: AuthenticationManager
    ): MainMenuMvp.Presenter =
        MainMenuPresenter(model, activityContract, authenticationManager)

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

    // Leaderboard Screen

    @Provides
    fun provideLeaderboardModel(
        networkRepository: NetworkRepository,
        databaseRepository: DatabaseRepository
    ): LeaderboardMvp.Model =
        LeaderboardModel(networkRepository, databaseRepository)

    @Provides
    fun provideLeaderboardPresenter(
        model: LeaderboardMvp.Model,
        activityContract: ActivityContract
    ): LeaderboardMvp.Presenter =
        LeaderboardPresenter(model, activityContract)

    // Moderate Reports Screen

    @Provides
    fun provideModerateReportsModel(
        networkRepository: NetworkRepository
    ): ModerateReportsMvp.Model = ModerateReportsModel(networkRepository)

    @Provides
    fun provideModerateReportsPresenter(
        model: ModerateReportsMvp.Model,
        activityContract: ActivityContract
    ): ModerateReportsMvp.Presenter = ModerateReportsPresenter(model, activityContract)

    // Settings Screen

    @Provides
    fun provideSettingsModel(networkRepository: NetworkRepository): SettingsMvp.Model =
        SettingsModel(networkRepository)

    @Provides
    fun provideSettingsPresenter(
        model: SettingsMvp.Model,
        activityContract: ActivityContract,
        authenticationManager: AuthenticationManager
    ): SettingsMvp.Presenter = SettingsPresenter(model, activityContract, authenticationManager)
}