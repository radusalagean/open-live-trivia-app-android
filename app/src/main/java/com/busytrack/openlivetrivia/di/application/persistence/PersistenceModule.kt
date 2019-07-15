package com.busytrack.openlivetrivia.di.application.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class PersistenceModule {
    @Provides
    @ApplicationScope
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}