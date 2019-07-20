package com.busytrack.openlivetrivia.di.application.auth

import android.content.Context
import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.auth.AuthenticationRepository
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {
    @Provides
    @ApplicationScope
    fun provideGoogleSignInClient(
        context: Context,
        googleSignInOptions: GoogleSignInOptions
    ): GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    @Provides
    @ApplicationScope
    fun provideGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_SERVER_CLIENT_ID)
            .build()

    @Provides
    @ApplicationScope
    fun provideAuthenticationRepository(): AuthenticationRepository = AuthenticationRepository()
}