package com.busytrack.openlivetrivia.di.application.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.busytrack.openlivetrivia.di.NAMED_APPLICATION_CONTEXT
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.busytrack.openlivetrivia.persistence.database.AppDatabase
import com.busytrack.openlivetrivia.persistence.database.DatabaseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PersistenceModule {
    @Provides
    @ApplicationScope
    fun provideSharedPreferences(@Named(NAMED_APPLICATION_CONTEXT) context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @ApplicationScope
    fun provideDatabase(@Named(NAMED_APPLICATION_CONTEXT) context: Context): AppDatabase =
        AppDatabase.buildDatabase(context)

    @Provides
    @ApplicationScope
    fun provideDatabaseRepository(database: AppDatabase): DatabaseRepository =
        DatabaseRepository(database)
}