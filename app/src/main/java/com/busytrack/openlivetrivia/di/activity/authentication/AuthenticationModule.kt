package com.busytrack.openlivetrivia.di.activity.authentication

import android.content.Context
import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {
    @Provides
    @ActivityScope
    fun provideGoogleSignInClient(
        context: Context,
        googleSignInOptions: GoogleSignInOptions
    ): GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    @Provides
    @ActivityScope
    fun provideGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_SERVER_CLIENT_ID)
            .build()

    @Provides
    @ActivityScope
    fun provideAuthenticationManager(
        googleSignInClient: GoogleSignInClient,
        firebaseAuth: FirebaseAuth,
        activityContract: ActivityContract
    ): AuthenticationManager = AuthenticationManager(googleSignInClient, firebaseAuth, activityContract)
}