package com.busytrack.openlivetrivia.auth

import android.content.Intent
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber

class AuthenticationManager(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth,
    private val activityContract: ActivityContract
) {

    fun signIn() {
        activityContract.triggerGoogleSignIn(googleSignInClient.signInIntent)
    }

    /**
     * [silent] sign out is suitable for cases where we don't want the usual activity handling of the event
     */
    fun signOut(silent: Boolean = false) {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
        googleSignInClient.revokeAccess()
        sharedPreferencesRepository.clearAuthenticatedAccount()
        if (!silent) {
            activityContract.handleLogOut()
        }
    }

    fun handleGoogleSignInSuccess(data: Intent?) {
        // Google log in was successful, authenticate with Firebase
        try {
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            authenticateWithFirebase(accountTask.result)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun handleGoogleSignInFailure(resultCode: Int) {
        activityContract.handleFailedFirebaseLogIn(Exception("Result code: $resultCode"))
    }

    fun setAuthenticatedUser(userModel: UserModel) {
        sharedPreferencesRepository.updateAuthenticatedAccount(userModel)
    }

    fun getAuthenticatedUser() = sharedPreferencesRepository.getAuthenticatedAccount()

    private fun authenticateWithFirebase(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    activityContract.handleSuccessfulFirebaseLogIn()
                } else {
                    activityContract.handleFailedFirebaseLogIn(it.exception)
                }
            }
    }
}