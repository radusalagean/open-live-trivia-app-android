package com.busytrack.openlivetrivia.generic.activity

import android.content.Intent
import androidx.annotation.IdRes

interface ActivityContract {
    fun showInfoMessage(message: Int, args: Any? = null)
    fun showWarningMessage(message: Int, args: Any? = null)
    fun showErrorMessage(message: Int, args: Any? = null)
    fun triggerGoogleSignIn(intent: Intent)
    @IdRes fun getFragmentContainerId(): Int
    fun popAllFragments()
    fun showAuthenticationScreen()
    fun showMainMenuScreen()
    fun showGameScreen()
    fun showLeaderboardScreen()
    fun handleSuccessfulFirebaseLogIn()
    fun handleFailedFirebaseLogIn(t: Throwable?)
    fun handleLogOut()
}