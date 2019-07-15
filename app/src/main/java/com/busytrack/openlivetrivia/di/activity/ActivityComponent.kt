package com.busytrack.openlivetrivia.di.activity

import com.busytrack.openlivetrivia.activity.MainActivity
import com.busytrack.openlivetrivia.di.activity.authentication.AuthenticationModule
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    ActivityModule::class,
    AuthenticationModule::class
])
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
    // TODO inject fragments
}