package com.busytrack.openlivetrivia.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
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
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    val firebaseAuth: FirebaseAuth,
    private val googleIdOption: GetGoogleIdOption,
    private val credentialManager: CredentialManager
) : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun signIn(
        activityContext: Context,
        activityContract: ActivityContract,
        onError: (message: String) -> Unit
    ) {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        launch {
            try {
                val result = credentialManager.getCredential(
                    context = activityContext,
                    request = request
                )

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                activityContract.firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
            } catch (e: GetCredentialException) {
                var message = e.localizedMessage
                if (e is NoCredentialException) {
                    message = "Please make sure you are signed into your device with a Google account first."
                }
                onError("Couldn't retrieve user's credentials: $message")
            }
        }
    }

    /**
     * [silent] don't show log out message
     */
    fun signOut(silent: Boolean = false, activityContract: ActivityContract? = null) {
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
        if (!silent) activityContract?.showLogOutMessage()
    }

    fun setAuthenticatedUser(userModel: UserModel) {
        sharedPreferencesRepository.updateAuthenticatedAccount(userModel)
    }

    fun getAuthenticatedUser() = sharedPreferencesRepository.getAuthenticatedAccount()
}