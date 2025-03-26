package com.busytrack.openlivetrivia.di.application.network

import com.busytrack.openlivetrivia.generic.scheduler.BaseSchedulerProvider
import com.busytrack.openlivetrivia.network.authenticator.OpenLiveTriviaAuthenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Retrofit API Module Contract
 *
 * @param <T> - API Service Type to be provided
 */
interface ApiModuleContract<T> {
    /**
     * Return the base URL of the API service
     */
    fun getBaseUrl(): String

    /**
     * Provide the {@link OkHttpClient} instance
     */
    fun provideClient(
        loggingInterceptor: Interceptor?,
        stethoInterceptor: Interceptor?,
        headerInterceptor: Interceptor,
        openLiveTriviaAuthenticator: OpenLiveTriviaAuthenticator
    ): OkHttpClient

    /**
     * Provide the {@link Retrofit} instance
     */
    fun provideRetrofit(
        baseUrl: String,
        schedulerProvider: BaseSchedulerProvider,
        okHttpClient: OkHttpClient
    ): Retrofit

    /**
     * Provide the API Service of type {@link T}
     */
    fun provideApiService(retrofit: Retrofit): T
}