package com.busytrack.openlivetrivia.di.application.network

import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.busytrack.openlivetrivia.network.NAMED_HEADER_INTERCEPTOR
import com.busytrack.openlivetrivia.network.NAMED_LOGGING_INTERCEPTOR
import com.busytrack.openlivetrivia.network.NAMED_STETHO_INTERCEPTOR
import com.busytrack.openlivetrivia.network.OKHTTP_TAG
import com.busytrack.openlivetrivia.network.interceptor.HeaderInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Named

@Module
class BaseApiModule {
    @Provides
    @Named(NAMED_LOGGING_INTERCEPTOR)
    @ApplicationScope
    fun provideLoggingInterceptor(): Interceptor? {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor { message ->
                Timber.tag(OKHTTP_TAG).d(message)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            null
        }
    }

    @Provides
    @Named(NAMED_STETHO_INTERCEPTOR)
    @ApplicationScope
    fun provideStethoInterceptor(): Interceptor? =
        if (BuildConfig.DEBUG) StethoInterceptor() else null

    @Provides
    @Named(NAMED_HEADER_INTERCEPTOR)
    @ApplicationScope
    fun provideHeaderInterceptor(authorizationManager: AuthorizationManager): Interceptor =
        HeaderInterceptor(authorizationManager)
}