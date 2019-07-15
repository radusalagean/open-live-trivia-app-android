package com.busytrack.openlivetrivia.generic.activity

import android.content.Intent

interface ActivityContract {
    fun showInfoMessage(message: String)
    fun showWarningMessage(message: String)
    fun showErrorMessage(message: String)
    fun triggerGoogleSignIn(intent: Intent)
}