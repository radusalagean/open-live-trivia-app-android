package com.busytrack.openlivetrivia.di.activity

import com.busytrack.openlivetrivia.activity.MainActivity
import com.busytrack.openlivetrivia.di.activity.authentication.AuthenticationModule
import com.busytrack.openlivetrivia.di.activity.mvp.MvpModule
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    ActivityModule::class,
    AuthenticationModule::class,
    MvpModule::class
])
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(authenticationFragment: AuthenticationFragment)
}