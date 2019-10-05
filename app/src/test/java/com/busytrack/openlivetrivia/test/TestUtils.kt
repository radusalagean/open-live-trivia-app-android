package com.busytrack.openlivetrivia.test

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.busytrack.openlivetrivia.application.OpenLiveTriviaApp
import com.busytrack.openlivetriviainterface.rest.OpenLiveTriviaApiService
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.mockito.Mockito
import org.mockito.internal.util.reflection.FieldSetter
import org.robolectric.util.ReflectionHelpers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Set the android SDK version for the current unit test instance
 */
fun setSdkVersion(version: Int) {
    ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", version)
}

/**
 * Assigns the source field to the specified target through reflection
 */
fun assignFieldToTarget(target: Any, targetFieldName: String, source: Any?) {
    FieldSetter.setField(
        target,
        target.javaClass.getDeclaredField(targetFieldName),
        source
    )
}

/**
 * Configures Firebase for Robolectric tests
 */
fun setUpFirebase() {
    val firebaseAppMock = Mockito.mock(FirebaseApp::class.java)
    doReturn(Mockito.mock(FirebaseAuth::class.java))
        .`when`(firebaseAppMock).get<FirebaseAuth>(any())
    (ApplicationProvider.getApplicationContext<Context>() as OpenLiveTriviaApp)
        .firebaseAppMock = firebaseAppMock
}

/**
 * Returns the Retrofit API service used for unit testing
 */
fun getOpenLiveTriviaTestApiService(
    server: MockWebServer,
    client: OkHttpClient = OkHttpClient()
): OpenLiveTriviaApiService {

    return Retrofit.Builder()
        .baseUrl(server.url("/"))
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(OpenLiveTriviaApiService::class.java)
}