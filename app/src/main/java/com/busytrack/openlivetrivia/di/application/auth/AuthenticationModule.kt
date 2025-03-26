package com.busytrack.openlivetrivia.di.application.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.di.NAMED_APPLICATION_CONTEXT
import com.busytrack.openlivetrivia.di.application.ApplicationScope
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AuthenticationModule {
    @Provides
    @ApplicationScope
    fun provideGetCredentialRequest(
        getGoogleIdOption: GetGoogleIdOption
    ): GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(getGoogleIdOption)
        .build()

    @Provides
    @ApplicationScope
    fun provideGetGoogleIdOption(): GetGoogleIdOption =
        GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.FIREBASE_SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .build()

    @Provides
    @ApplicationScope
    fun provideCredentialManager(
        @Named(NAMED_APPLICATION_CONTEXT) context: Context,
    ): CredentialManager = CredentialManager.create(context)

    @Provides
    @ApplicationScope
    fun provideAuthenticationManager(
        sharedPreferencesRepository: SharedPreferencesRepository,
        firebaseAuth: FirebaseAuth,
        googleIdOption: GetGoogleIdOption,
        credentialManager: CredentialManager
    ): AuthenticationManager = AuthenticationManager(
        sharedPreferencesRepository,
        firebaseAuth,
        googleIdOption,
        credentialManager
    )
}