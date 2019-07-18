package com.busytrack.openlivetrivia.auth

import android.content.Intent
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber
import java.lang.Exception

class AuthenticationManager(
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth,
    private val activityContract: ActivityContract
) {

    fun signIn() {
        activityContract.triggerGoogleSignIn(googleSignInClient.signInIntent)
    }

    fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
        googleSignInClient.revokeAccess()
        activityContract.handleLogOut()
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