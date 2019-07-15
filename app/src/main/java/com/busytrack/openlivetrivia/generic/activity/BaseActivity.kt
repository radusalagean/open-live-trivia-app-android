package com.busytrack.openlivetrivia.generic.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.busytrack.openlivetrivia.application.OpenLiveTriviaApp
import com.busytrack.openlivetrivia.di.activity.ActivityComponent
import com.busytrack.openlivetrivia.di.activity.ActivityModule
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), ActivityContract {
    private val tag : String = javaClass.simpleName

    protected val activityComponent: ActivityComponent by lazy {
        (application as OpenLiveTriviaApp).applicationComponent
            .newActivityComponent(ActivityModule(this))
    }

    // Lifecycle callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(tag).v("-A-> onCreate($savedInstanceState)")
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Timber.tag(tag).v("-A-> onStart()")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Timber.tag(tag).v("-A-> onRestoreInstanceState($savedInstanceState)")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        Timber.tag(tag).v("-A-> onResume()")
        super.onResume()
    }

    override fun onPause() {
        Timber.tag(tag).v("-A-> onPause()")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.tag(tag).v("-A-> onSaveInstanceState($outState)")
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        Timber.tag(tag).v("-A-> onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.tag(tag).v("-A-> onDestroy()")
        super.onDestroy()
    }

    // Other activity callbacks

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.tag(tag).v("-A-> onActivityResult($requestCode, $resultCode, $data)")
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GOOGLE_SIGN_IN_REQUEST_CODE -> handleGoogleSignInResult(resultCode, data)
        }
    }

    // Activity contract implementation

    override fun showInfoMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showWarningMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun triggerGoogleSignIn(intent: Intent) {
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    // Abstract methods

    abstract fun handleGoogleSignInResult(resultCode: Int, data: Intent?)
}