package com.busytrack.openlivetrivia.auth

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth

class AuthorizationManager(
    private val firebaseAuth: FirebaseAuth
) {

    fun executeWithToken(
        successCallback: (idToken: String) -> Unit,
        failCallback: ((reason: String) -> Unit)? = null
    ) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.getIdToken(false)?.addOnCompleteListener {
            if (it.result == null || it.result?.token.isNullOrEmpty()) {
                failCallback?.let { it("Result is null or empty") }
            }
            it.result?.token?.let {idToken ->
                successCallback(idToken)
            }
        }
    }

    fun isUserAuthenticated(): Boolean = firebaseAuth.currentUser != null

    /**
     * Synchronous call!
     */
    fun getIdToken(): String? = firebaseAuth.currentUser?.let {
        Tasks.await(it.getIdToken(false))?.token
    }
}