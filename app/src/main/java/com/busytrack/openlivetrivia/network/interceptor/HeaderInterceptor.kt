package com.busytrack.openlivetrivia.network.interceptor

import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.google.common.net.HttpHeaders.AUTHORIZATION
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val authorizationManager: AuthorizationManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request().newBuilder().apply {
            if (authorizationManager.isUserAuthenticated()) {
                authorizationManager.getIdToken()?.let { idToken ->
                    //Timber.w("idToken: $idToken")
                    // Add the Firebase IdToken to all REST requests
                    addHeader(AUTHORIZATION, idToken)
                }
            }
        }.build())
}