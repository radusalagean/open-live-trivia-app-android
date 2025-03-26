package com.busytrack.openlivetrivia.generic.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.application.OpenLiveTriviaApp
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.di.activity.ActivityComponent
import com.busytrack.openlivetrivia.di.activity.ActivityModule
import com.busytrack.openlivetrivia.generic.eventbus.EventBus
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.infobar.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.firebase.auth.GoogleAuthProvider
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), ActivityContract, InfoBarContract {
    private val logTag : String = javaClass.simpleName

    abstract var authenticationManager: AuthenticationManager
    abstract var infoBarManager: InfoBarManager
    abstract var eventBus: EventBus

    // Dagger2 Activity Component lazy initialization
    val activityComponent: ActivityComponent by lazy {
        (application as OpenLiveTriviaApp).applicationComponent
            .newActivityComponent(ActivityModule(this))
    }

    // Lifecycle callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(logTag).v("-A-> onCreate($savedInstanceState)")
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Timber.tag(logTag).v("-A-> onStart()")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Timber.tag(logTag).v("-A-> onRestoreInstanceState($savedInstanceState)")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        Timber.tag(logTag).v("-A-> onResume()")
        super.onResume()
        infoBarManager.resume(this)
    }

    override fun onPause() {
        Timber.tag(logTag).v("-A-> onPause()")
        infoBarManager.pause()
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

    override fun attachBaseContext(newBase: Context?) {
        // Step required for custom fonts implementation w/ calligraphy lib
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    /**
     * First, pass the event to the current fragment
     * If it's not handled there, let the activity handle it
     */
    override fun onBackPressed() {
        getCurrentFragment()?.let {
            if (!it.onBackPressed()) {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

    // Base methods

    fun getCurrentFragment() =
        supportFragmentManager.findFragmentById(getFragmentContainerId())
            ?.childFragmentManager?.fragments
            ?.firstOrNull { it is BaseFragment<*> && it.isVisible } as? BaseFragment<*>

    // Activity contract implementation

    override fun showInfoMessage(message: Int, vararg args: Any?) {
        enqueueMessage(TYPE_INFO, message, *args)
    }

    override fun showWarningMessage(message: Int, vararg args: Any?) {
        enqueueMessage(TYPE_WARN, message, *args)
    }

    override fun showErrorMessage(message: Int, vararg args: Any?) {
        enqueueMessage(TYPE_ERROR, message, *args)
    }

    override fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        authenticationManager.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    handleSuccessfulFirebaseLogIn()
                } else {
                    handleFailedFirebaseLogIn(task.exception)
                }
            }
    }

    override fun handleSuccessfulFirebaseLogIn() {
        showInfoMessage(R.string.message_logged_in_successfully)
        // Pass the event to the current fragment
        getCurrentFragment()?.handleSuccessfulFirebaseSignIn()
    }

    override fun handleFailedFirebaseLogIn(t: Throwable?) {
        showErrorMessage(R.string.message_failed_to_log_in, t?.message)
        // Pass the event to the current fragment
        getCurrentFragment()?.handleFailedFirebaseSignIn(t)
    }

    override fun showLogOutMessage() {
        showInfoMessage(R.string.message_logged_out)
    }

    override fun openLinkInBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    // InfoBar Contract

    /**
     * Call in order to display an [InfoBar] message immediately
     */
    override fun showInfoBarNow(
        infoBarConfiguration: InfoBarConfiguration,
        callback: BaseTransientBottomBar.BaseCallback<InfoBar>
    ) {
        getCurrentFragment()?.let {
            InfoBar.make(
                it.getInfoBarContainer(),
                infoBarConfiguration.message,
                infoBarConfiguration.type
            ).addCallback(callback).show()
        }
    }

    // Abstract methods

    /**
     * Override to inject dependencies
     */
    protected open fun injectDependencies() {
        // empty implementation
    }

    // Private

    /**
     * Enqueue the [InfoBar] message
     */
    private fun enqueueMessage(type: Int, message: Int, vararg args: Any?) {
        val string = if (args.isEmpty()) getString(message) else getString(message, *args)
        getCurrentFragment()?.let {
            infoBarManager.enqueueMessage(InfoBarConfiguration(string, type))
        }
    }
}