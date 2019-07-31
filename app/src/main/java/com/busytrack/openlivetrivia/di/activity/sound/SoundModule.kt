package com.busytrack.openlivetrivia.di.activity.sound

import android.content.Context
import com.busytrack.openlivetrivia.di.NAMED_ACTIVITY_CONTEXT
import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.busytrack.openlivetrivia.sound.SoundManager
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SoundModule {

    @ActivityScope
    @Provides
    fun provideSoundManager(
        @Named(NAMED_ACTIVITY_CONTEXT) context: Context,
        sharedPreferencesRepository: SharedPreferencesRepository
    ): SoundManager =
        SoundManager(context, sharedPreferencesRepository)
}