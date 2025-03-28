package com.busytrack.openlivetrivia.di.activity

import com.busytrack.openlivetrivia.activity.MainActivity
import com.busytrack.openlivetrivia.di.activity.dialog.DialogModule
import com.busytrack.openlivetrivia.di.activity.infobar.InfoBarModule
import com.busytrack.openlivetrivia.di.activity.mvp.MvpModule
import com.busytrack.openlivetrivia.di.activity.rights.RightsModule
import com.busytrack.openlivetrivia.di.activity.sound.SoundModule
import com.busytrack.openlivetrivia.di.activity.vibration.VibrationModule
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationFragment
import com.busytrack.openlivetrivia.screen.game.GameFragment
import com.busytrack.openlivetrivia.screen.leaderboard.LeaderboardFragment
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuFragment
import com.busytrack.openlivetrivia.screen.moderatereports.ModerateReportsFragment
import com.busytrack.openlivetrivia.screen.settings.SettingsFragment
import com.busytrack.openlivetrivia.view.TimeTextView
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    ActivityModule::class,
    MvpModule::class,
    DialogModule::class,
    RightsModule::class,
    InfoBarModule::class,
    VibrationModule::class,
    SoundModule::class
])
interface ActivityComponent {
    // Activity
    fun inject(mainActivity: MainActivity)
    // Fragment
    fun inject(authenticationFragment: AuthenticationFragment)
    fun inject(mainMenuFragment: MainMenuFragment)
    fun inject(gameFragment: GameFragment)
    fun inject(leaderboardFragment: LeaderboardFragment)
    fun inject(moderateReportsFragment: ModerateReportsFragment)
    fun inject(settingsFragment: SettingsFragment)
    // View
    fun inject(timeTextView: TimeTextView)
}