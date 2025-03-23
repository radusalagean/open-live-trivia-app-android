package com.busytrack.openlivetrivia.di.application.auth

import android.app.Application
import com.busytrack.openlivetrivia.application.OpenLiveTriviaApp
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class AuthorizationModule {
    @Provides
    @ApplicationScope
    fun provideFirebaseAuth(application: Application): FirebaseAuth {
        return (application as OpenLiveTriviaApp).firebaseAppMock?.let {
            // if the firebaseApp reference is not null, use it as a parameter
            Firebase.auth(it)
        } ?: Firebase.auth
    }

    @Provides
    @ApplicationScope
    fun provideAuthorizationManager(firebaseAuth: FirebaseAuth): AuthorizationManager =
        AuthorizationManager(firebaseAuth)
}