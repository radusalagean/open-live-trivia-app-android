package com.busytrack.openlivetrivia.generic.activity

import android.content.Intent
import androidx.annotation.IdRes

interface ActivityContract {
    fun showInfoMessage(message: String)
    fun showWarningMessage(message: String)
    fun showErrorMessage(message: String)
    fun triggerGoogleSignIn(intent: Intent)
    @IdRes fun getFragmentContainerId(): Int
    fun popAllFragments()
    fun handleSuccessfulFirebaseLogIn()
    fun handleFailedFirebaseLogIn(t: Throwable?)
    fun handleLogOut()
}