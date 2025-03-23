package com.busytrack.openlivetrivia.di.activity.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.di.NAMED_ACTIVITY_CONTEXT
import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AuthenticationModule {
    @Provides
    @ActivityScope
    fun provideAuthenticationManager(
        @Named(NAMED_ACTIVITY_CONTEXT) activityContext: Context,
        sharedPreferencesRepository: SharedPreferencesRepository,
        firebaseAuth: FirebaseAuth,
        activityContract: ActivityContract,
        googleIdOption: GetGoogleIdOption,
        credentialManager: CredentialManager
    ): AuthenticationManager = AuthenticationManager(
        activityContext,
        sharedPreferencesRepository,
        firebaseAuth,
        activityContract,
        googleIdOption,
        credentialManager
    )
}