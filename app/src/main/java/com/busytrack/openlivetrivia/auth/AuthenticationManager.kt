package com.busytrack.openlivetrivia.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class AuthenticationManager(
    private val activityContext: Context,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    val firebaseAuth: FirebaseAuth,
    private val activityContract: ActivityContract,
    private val googleIdOption: GetGoogleIdOption,
    private val credentialManager: CredentialManager
) : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun signIn(
        onError: (message: String) -> Unit
    ) {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    context = activityContext,
                    request = request
                )

                // Extract credential from the result returned by Credential Manager
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                onError("Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        // Create Google ID Token
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

        // Sign in to Firebase with using the token
        activityContract.firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
    }

    /**
     * [silent] don't show log out message
     */
    fun signOut(silent: Boolean = false) {
        firebaseAuth.signOut()
        launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
            } catch (e: ClearCredentialException) {
                Timber.e("Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
        sharedPreferencesRepository.clearAuthenticatedAccount()
        if (!silent) activityContract.showLogOutMessage()
    }

    fun setAuthenticatedUser(userModel: UserModel) {
        sharedPreferencesRepository.updateAuthenticatedAccount(userModel)
    }

    fun getAuthenticatedUser() = sharedPreferencesRepository.getAuthenticatedAccount()
}