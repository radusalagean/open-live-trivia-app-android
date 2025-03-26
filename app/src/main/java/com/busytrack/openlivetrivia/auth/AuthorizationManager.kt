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
    fun getIdToken(forceRefresh: Boolean = false): String? = try {
        firebaseAuth.currentUser?.let {
            Tasks.await(it.getIdToken(forceRefresh))?.token
        }
    } catch (e: Exception) {
        null
    }
}