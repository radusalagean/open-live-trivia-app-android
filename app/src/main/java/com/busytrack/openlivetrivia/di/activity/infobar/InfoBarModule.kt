package com.busytrack.openlivetrivia.di.activity.infobar

import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.infobar.InfoBarManager
import dagger.Module
import dagger.Provides

@Module
class InfoBarModule {

    @ActivityScope
    @Provides
    fun provideInfoBarManager(): InfoBarManager = InfoBarManager()
}