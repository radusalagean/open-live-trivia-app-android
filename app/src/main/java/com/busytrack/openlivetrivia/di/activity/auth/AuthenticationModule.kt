package com.busytrack.openlivetrivia.di.activity.auth

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.AuthenticationRepository
import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {
    @Provides
    @ActivityScope
    fun provideAuthenticationManager(
        authenticationRepository: AuthenticationRepository,
        googleSignInClient: GoogleSignInClient,
        firebaseAuth: FirebaseAuth,
        activityContract: ActivityContract
    ): AuthenticationManager = AuthenticationManager(
        authenticationRepository,
        googleSignInClient,
        firebaseAuth,
        activityContract
    )
}