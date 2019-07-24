package com.busytrack.openlivetrivia.auth

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth

class AuthorizationManager(
    private val firebaseAuth: FirebaseAuth
) {

    fun isUserAuthenticated(): Boolean = firebaseAuth.currentUser != null

    /**
     * Synchronous call!
     */
    fun getIdToken(): String? = firebaseAuth.currentUser?.let {
        Tasks.await(it.getIdToken(false))?.token
    }
}