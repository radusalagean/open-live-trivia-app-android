package com.busytrack.openlivetrivia.generic.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.busytrack.openlivetrivia.application.OpenLiveTriviaApp
import com.busytrack.openlivetrivia.di.activity.ActivityComponent
import com.busytrack.openlivetrivia.di.activity.ActivityModule
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import timber.log.Timber
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment

abstract class BaseActivity : AppCompatActivity(), ActivityContract {
    private val logTag : String = javaClass.simpleName

    protected val activityComponent: ActivityComponent by lazy {
        (application as OpenLiveTriviaApp).applicationComponent
            .newActivityComponent(ActivityModule(this))
    }

    // Lifecycle callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(logTag).v("-A-> onCreate($savedInstanceState)")
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Timber.tag(logTag).v("-A-> onStart()")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Timber.tag(logTag).v("-A-> onRestoreInstanceState($savedInstanceState)")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        Timber.tag(logTag).v("-A-> onResume()")
        super.onResume()
    }

    override fun onPause() {
        Timber.tag(logTag).v("-A-> onPause()")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.tag(logTag).v("-A-> onSaveInstanceState($outState)")
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        Timber.tag(logTag).v("-A-> onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.tag(logTag).v("-A-> onDestroy()")
        super.onDestroy()
    }

    // Other activity callbacks

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.tag(logTag).v("-A-> onActivityResult($requestCode, $resultCode, $data)")
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GOOGLE_SIGN_IN_REQUEST_CODE -> handleGoogleSignInResult(resultCode, data)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    // Base methods

    /**
     * Adds a default fragment if no fragment is present for the specified container
     */
    protected fun addDefaultFragmentIfNecessary() {
        val currentFragment = getCurrentFragment()
        if (currentFragment == null) {
            val defaultFragment = getDefaultFragment()
            Timber.d("No fragment was previously attached, attaching %s as starting point", defaultFragment)
            showFragment(defaultFragment, false, null)
        }
    }

    fun getCurrentFragment() =
        supportFragmentManager.findFragmentById(getFragmentContainerId()) as BaseFragment?

    /**
     * Show a fragment
     */
    protected fun <T : BaseFragment> showFragment(
        fragment: T,
        addToBackStack: Boolean = true,
        backStackStateName: String? = null
    ) {
        // Make sure we don't open unnecessary fragments
        with(getCurrentFragment()) {
            if (this != null && javaClass == fragment.javaClass) {
                Timber.d("Attempting to open an unnecessary fragment, skipping request!")
                return
            }
        }
        supportFragmentManager.beginTransaction().apply {
            replace(getFragmentContainerId(), fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if (addToBackStack) {
                addToBackStack(backStackStateName)
            }
        }.commit()
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

    override fun popAllFragments() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun handleSuccessfulFirebaseSignIn() {
        showInfoMessage("Signed in successfully")
        // Pass the event to the current fragment
        getCurrentFragment()?.handleSuccessfulFirebaseSignIn()
    }

    override fun handleFailedFirebaseSignIn(t: Throwable?) {
        showErrorMessage("Failed to sign in: ${t?.message}")
        // Pass the event to the current fragment
        getCurrentFragment()?.handleFailedFirebaseSignIn(t)
    }

    override fun handleSignOut() {
        showInfoMessage("Signed out")
    }

    // Abstract methods

    abstract fun handleGoogleSignInResult(resultCode: Int, data: Intent?)

    /**
     * Override to specify the default fragment to be added with the [addDefaultFragmentIfNecessary] method
     */
    protected abstract fun getDefaultFragment(): BaseFragment
}