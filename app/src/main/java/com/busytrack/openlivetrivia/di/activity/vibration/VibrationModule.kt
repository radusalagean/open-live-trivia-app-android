package com.busytrack.openlivetrivia.di.activity.vibration

import android.content.Context
import android.os.Vibrator
import com.busytrack.openlivetrivia.di.NAMED_ACTIVITY_CONTEXT
import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.busytrack.openlivetrivia.vibration.VibrationManager
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class VibrationModule {

    @ActivityScope
    @Provides
    fun provideVibrator(@Named(NAMED_ACTIVITY_CONTEXT) context: Context): Vibrator =
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @ActivityScope
    @Provides
    fun provideVibrationManager(
        vibrator: Vibrator,
        sharedPreferencesRepository: SharedPreferencesRepository
    ): VibrationManager = VibrationManager(vibrator, sharedPreferencesRepository)
}