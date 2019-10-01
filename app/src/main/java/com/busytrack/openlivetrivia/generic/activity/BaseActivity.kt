package com.busytrack.openlivetrivia.generic.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.application.OpenLiveTriviaApp
import com.busytrack.openlivetrivia.di.activity.ActivityComponent
import com.busytrack.openlivetrivia.di.activity.ActivityModule
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import timber.log.Timber
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.infobar.*
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationFragment
import com.busytrack.openlivetrivia.screen.game.GameFragment
import com.busytrack.openlivetrivia.screen.leaderboard.LeaderboardFragment
import com.busytrack.openlivetrivia.screen.mainmenu.MainMenuFragment
import com.busytrack.openlivetrivia.screen.moderatereports.ModerateReportsFragment
import com.busytrack.openlivetrivia.screen.settings.SettingsFragment
import com.google.android.material.snackbar.BaseTransientBottomBar

abstract class BaseActivity : AppCompatActivity(), ActivityContract, InfoBarContract {
    private val logTag : String = javaClass.simpleName

    open lateinit var infoBarManager: InfoBarManager

    // Dagger2 Activity Component lazy initialization
    val activityComponent: ActivityComponent by lazy {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.tag(logTag).v("-A-> onActivityResult($requestCode, $resultCode, $data)")
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GOOGLE_LOG_IN_REQUEST_CODE -> handleGoogleSignInResult(resultCode, data)
        }
    }

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
        // Make sure the fragment is not already in the foreground
        with(getCurrentFragment()) {
            if (this != null && javaClass == fragment.javaClass) {
                Timber.d("Attempting to open an unnecessary fragment, skipping request!")
                return
            }
        }
        supportFragmentManager.beginTransaction().apply {
            replace(getFragmentContainerId(), fragment, fragment.javaClass.name)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if (addToBackStack) {
                addToBackStack(backStackStateName)
            }
        }.commit()
    }

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

    override fun triggerGoogleSignIn(intent: Intent) {
        startActivityForResult(intent, GOOGLE_LOG_IN_REQUEST_CODE)
    }

    override fun popAllFragments() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun showAuthenticationScreen() {
        showFragment(AuthenticationFragment.newInstance(), addToBackStack = false)
    }

    override fun showMainMenuScreen() {
        var mainMenuFragment: BaseFragment? = null
        // If the main menu fragment is already in the back stack, use it
        supportFragmentManager.findFragmentByTag(MainMenuFragment::class.java.name)?.let {
            mainMenuFragment = it as BaseFragment
        }
        showFragment(mainMenuFragment ?: MainMenuFragment.newInstance(), addToBackStack = false)
    }

    override fun showGameScreen() {
        showFragment(GameFragment.newInstance())
    }

    override fun showLeaderboardScreen() {
        showFragment(LeaderboardFragment.newInstance())
    }

    override fun showModerateReportsScreen() {
        showFragment(ModerateReportsFragment.newInstance())
    }

    override fun showSettingsScreen() {
        showFragment(SettingsFragment.newInstance())
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

    override fun handleLogOut() {
        showInfoMessage(R.string.message_logged_out)
        // Pass the event to the current fragment
        getCurrentFragment()?.handleLogOut()
        popAllFragments()
        showAuthenticationScreen()
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
     * Override to handle the event in the concrete Activity class
     */
    abstract fun handleGoogleSignInResult(resultCode: Int, data: Intent?)

    /**
     * Override to specify the default fragment to be added with the [addDefaultFragmentIfNecessary] method
     */
    protected abstract fun getDefaultFragment(): BaseFragment

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