package com.busytrack.openlivetrivia.di.application.auth

import android.content.Context
import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.di.NAMED_APPLICATION_CONTEXT
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AuthenticationModule {
    @Provides
    @ApplicationScope
    fun provideGoogleSignInClient(
        @Named(NAMED_APPLICATION_CONTEXT) context: Context,
        googleSignInOptions: GoogleSignInOptions
    ): GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    @Provides
    @ApplicationScope
    fun provideGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_SERVER_CLIENT_ID)
            .build()
}