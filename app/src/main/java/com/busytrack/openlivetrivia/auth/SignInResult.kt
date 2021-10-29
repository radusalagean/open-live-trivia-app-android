package com.busytrack.openlivetrivia.auth

import android.content.Intent

sealed class SignInResult {
    class Success(val intent: Intent) : SignInResult()
    class Failure(val resultCode: Int) : SignInResult()
}