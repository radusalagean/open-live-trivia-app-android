package com.busytrack.openlivetrivia.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class SignInResultContract : ActivityResultContract<Intent, SignInResult>() {

    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): SignInResult {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            SignInResult.Success(intent)
        } else {
            SignInResult.Failure(resultCode)
        }
    }
}