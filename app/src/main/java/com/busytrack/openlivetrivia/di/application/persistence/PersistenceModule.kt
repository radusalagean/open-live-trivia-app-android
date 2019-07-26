package com.busytrack.openlivetrivia.di.application.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.busytrack.openlivetrivia.di.NAMED_APPLICATION_CONTEXT
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PersistenceModule {
    @Provides
    @ApplicationScope
    fun provideSharedPreferences(@Named(NAMED_APPLICATION_CONTEXT) context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}