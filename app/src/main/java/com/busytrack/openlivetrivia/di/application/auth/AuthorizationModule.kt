package com.busytrack.openlivetrivia.di.application.auth

import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class AuthorizationModule {
    @Provides
    @ApplicationScope
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @ApplicationScope
    fun provideAuthorizationManager(firebaseAuth: FirebaseAuth): AuthorizationManager =
        AuthorizationManager(firebaseAuth)
}