package com.busytrack.openlivetrivia.di.activity

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.busytrack.openlivetrivia.di.NAMED_ACTIVITY_CONTEXT
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ActivityModule(private val activity: FragmentActivity) {
    @Provides
    @ActivityScope
    fun provideActivity(): FragmentActivity = activity

    @Provides
    @Named(NAMED_ACTIVITY_CONTEXT)
    @ActivityScope
    fun provideContext(): Context = activity

    @Provides
    @ActivityScope
    fun provideActivityContract(): ActivityContract = activity as ActivityContract
}