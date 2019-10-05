package com.busytrack.openlivetrivia.generic.activity

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.fragment.BaseFragmentImpl
import com.busytrack.openlivetrivia.infobar.InfoBarConfiguration
import com.busytrack.openlivetrivia.infobar.TYPE_ERROR
import com.busytrack.openlivetrivia.infobar.TYPE_INFO
import com.busytrack.openlivetrivia.infobar.TYPE_WARN
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationFragment
import com.busytrack.openlivetrivia.test.setUpFirebase
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class BaseActivityTest {

    private lateinit var activityScenario: ActivityScenario<BaseActivityImpl>

    @Before
    fun setUp() {
        setUpFirebase()
        activityScenario = ActivityScenario.launch(BaseActivityImpl::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun should_resumeInfoBarManager_when_activityIsResumed() {
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        activityScenario.onActivity {
            verify(it.infoBarManager).resume(it)
        }
    }

    @Test
    fun should_pauseInfoBarManager_when_activityIsSPaused() {
        // move back to started state from resumed state
        activityScenario.moveToState(Lifecycle.State.STARTED)

        activityScenario.onActivity {
            verify(it.infoBarManager).pause()
        }
    }

    @Test
    fun should_enqueueMessage_when_showInfoMessageIsCalled() {
        activityScenario.onActivity {
            val spyActivity = spy(it)
            doReturn(mock(BaseFragment::class.java))
                .`when`(spyActivity).getCurrentFragment()

            spyActivity.showInfoMessage(R.string.test_info_message, "arg1", "arg2")

            verify(spyActivity.infoBarManager)
                .enqueueMessage(
                    eq(InfoBarConfiguration(
                        "This is a test info message: arg1 arg2",
                        TYPE_INFO
                    ))
                )
        }
    }

    @Test
    fun should_enqueueMessage_when_showWarningMessageIsCalled() {
        activityScenario.onActivity {
            val spyActivity = spy(it)
            doReturn(mock(BaseFragment::class.java))
                .`when`(spyActivity).getCurrentFragment()

            spyActivity.showWarningMessage(R.string.test_info_message, "arg1", "arg2")

            verify(spyActivity.infoBarManager)
                .enqueueMessage(
                    eq(InfoBarConfiguration(
                        "This is a test info message: arg1 arg2",
                        TYPE_WARN
                    ))
                )
        }
    }

    @Test
    fun should_enqueueMessage_when_showErrorMessageIsCalled() {
        activityScenario.onActivity {
            val spyActivity = spy(it)
            doReturn(mock(BaseFragment::class.java))
                .`when`(spyActivity).getCurrentFragment()

            spyActivity.showErrorMessage(R.string.test_info_message, "arg1", "arg2")

            verify(spyActivity.infoBarManager)
                .enqueueMessage(
                    eq(InfoBarConfiguration(
                        "This is a test info message: arg1 arg2",
                        TYPE_ERROR
                    ))
                )
        }
    }

    @Test
    fun should_popAllBackStackFragments_when_popAllFragmentsIsCalled() {
        activityScenario.onActivity {
            val initialFragment = it.getCurrentFragment()

            // Show the settings screen
            it.showSettingsScreen()

            // Show the leaderboard screen
            it.showLeaderboardScreen()

            // Check if the current fragment is not the initial fragment
            assertThat(it.getCurrentFragment()).isNotSameInstanceAs(initialFragment)

            // Pop
            it.popAllFragments()

            // Check id the current fragment is the initial fragment
            assertThat(it.getCurrentFragment()).isSameInstanceAs(initialFragment)
        }
    }

    @Test
    fun should_useTheSameMainFragmentInstance_when_showMainScreenIsCalledMultipleTimes() {
        activityScenario.onActivity {
            // Show the Main Menu Screen
            it.showMainMenuScreen()

            // Get a reference to it
            val originalMainMenuFragment = it.getCurrentFragment()

            // Show a different screen on top of it (Settings)
            it.showSettingsScreen()

            // Show the Main Menu Screen again
            it.showMainMenuScreen()

            // Check if the current fragment is the same instance as the original fragment
            assertThat(it.getCurrentFragment())
                .isSameInstanceAs(originalMainMenuFragment)
        }
    }

    private fun prepareSpyFragment(activity: BaseActivityImpl): BaseFragmentImpl {
        // Remove the current fragment
        activity.getCurrentFragment()!!.removeFragment()

        // Make sure there is no fragment set
        assertThat(activity.getCurrentFragment()).isNull()

        // Show a spy fragment
        val spyFragment = spy(BaseFragmentImpl())
        activity.showTestFragment(spyFragment)

        return spyFragment
    }

    @Test
    fun should_passTheEventToTheFragment_when_handleSuccessfulFirebaseLoginIsCalled() {
        activityScenario.onActivity {
            val spyFragment = prepareSpyFragment(it)

            // Handle Successful Login
            it.handleSuccessfulFirebaseLogIn()

            // Verify behavior
            verify(spyFragment)
                .handleSuccessfulFirebaseSignIn()
        }
    }

    @Test
    fun should_passTheEventToTheFragment_when_handleFailedFirebaseLoginIsCalled() {
        activityScenario.onActivity {
            val spyFragment = prepareSpyFragment(it)
            val throwable = Throwable()

            // Handle Failed Login
            it.handleFailedFirebaseLogIn(throwable)

            // Verify behavior
            verify(spyFragment)
                .handleFailedFirebaseSignIn(eq(throwable))
        }
    }

    @Test
    fun should_passTheEventToTheFragment_when_handleLogOutIsCalled() {
        activityScenario.onActivity {
            val spyFragment = prepareSpyFragment(it)

            // Handle LogOut
            it.handleLogOut()

            // Verify behavior
            verify(spyFragment).handleLogOut()
        }
    }

    @Test
    fun should_showAuthenticationScreen_when_handleLogOutIsCalled() {
        activityScenario.onActivity {
            // Handle LogOut
            it.handleLogOut()

            // Verify behavior
            assertThat(it.getCurrentFragment()).isInstanceOf(AuthenticationFragment::class.java)
        }
    }
}