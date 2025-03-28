package com.busytrack.openlivetrivia.di.application.network

import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.busytrack.openlivetrivia.generic.scheduler.BaseSchedulerProvider
import com.busytrack.openlivetrivia.network.NAMED_BASE_URL
import com.busytrack.openlivetrivia.network.NAMED_HEADER_INTERCEPTOR
import com.busytrack.openlivetrivia.network.NAMED_LOGGING_INTERCEPTOR
import com.busytrack.openlivetrivia.network.NAMED_STETHO_INTERCEPTOR
import com.busytrack.openlivetrivia.network.authenticator.OpenLiveTriviaAuthenticator
import com.busytrack.openlivetriviainterface.BuildConfig.API_PATH
import com.busytrack.openlivetriviainterface.BuildConfig.ROOT_DOMAIN
import com.busytrack.openlivetriviainterface.rest.OpenLiveTriviaApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module(includes = [BaseApiModule::class])
class OpenLiveTriviaApiModule : ApiModuleContract<OpenLiveTriviaApiService> {
    @Provides
    @Named(NAMED_BASE_URL)
    @ApplicationScope
    override fun getBaseUrl(): String = "$ROOT_DOMAIN$API_PATH"

    @Provides
    @ApplicationScope
    override fun provideClient(
        @Named(NAMED_LOGGING_INTERCEPTOR) loggingInterceptor: Interceptor?,
        @Named(NAMED_STETHO_INTERCEPTOR) stethoInterceptor: Interceptor?,
        @Named(NAMED_HEADER_INTERCEPTOR) headerInterceptor: Interceptor,
        openLiveTriviaAuthenticator: OpenLiveTriviaAuthenticator
    ): OkHttpClient = OkHttpClient.Builder().apply {
        loggingInterceptor?.let { addInterceptor(it) }
        stethoInterceptor?.let { addInterceptor(it) }
        addInterceptor(headerInterceptor)
        authenticator(openLiveTriviaAuthenticator)
    }.build()

    @Provides
    @ApplicationScope
    override fun provideRetrofit(
        @Named(NAMED_BASE_URL) baseUrl: String,
        schedulerProvider: BaseSchedulerProvider,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @ApplicationScope
    override fun provideApiService(retrofit: Retrofit): OpenLiveTriviaApiService =
        retrofit.create(OpenLiveTriviaApiService::class.java)
}