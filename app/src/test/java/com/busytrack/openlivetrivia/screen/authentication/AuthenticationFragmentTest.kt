package com.busytrack.openlivetrivia.screen.authentication

import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.android.synthetic.main.fragment_authentication.*
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class AuthenticationFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<AuthenticationFragmentTestImpl>

    @Before
    fun setUp() {
        // Launch AuthenticationFragment
        fragmentScenario = launchFragmentInContainer()
    }

    @After
    fun tearDown() {
        fragmentScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun should_checkServerCompatibility_when_fragmentIsStarted() {
        fragmentScenario.onFragment {
            // Verify
            verify(it.presenter).checkServerCompatibility()
        }
    }

    @Test
    fun should_updateRefreshIndicator_when_callbackIsReceived() {
        fragmentScenario.onFragment {
            // Check initial state
            assertThat(it.progress_bar_main.visibility).isNotEqualTo(View.VISIBLE)

            // Set indicator to true
            it.setRefreshingIndicator(true)

            // Verify current state
            assertThat(it.view_pager.visibility).isEqualTo(View.INVISIBLE)
            assertThat(it.progress_bar_main.visibility).isEqualTo(View.VISIBLE)
        }
    }

    @Test
    fun should_login_when_successfulFirebaseSignInCallbackReceived() {
        fragmentScenario.onFragment {
            // Call
            it.handleSuccessfulFirebaseSignIn()

            // Verify
            verify(it.presenter).login()
        }
    }

    @Test
    fun should_setRefreshingFlagToFalse_when_failedFirebaseSignInCallbackReceived() {
        fragmentScenario.onFragment {
            // Call
            it.handleFailedFirebaseSignIn(null)

            // Verify
            verify(it.presenter).refreshing = false
        }
    }

    @Test
    fun should_startWithLoginPage_when_fragmentIsStarted() {
        fragmentScenario.onFragment {
            assertThat(it.view_pager.currentItem).isEqualTo(AuthenticationPageType.LOG_IN.ordinal)
        }
    }

    @Test
    fun should_loginWithBackend_when_onLoginPressCalled_with_userAlreadyAuthenticatedLocally() {
        fragmentScenario.onFragment {
            // Stub
            doReturn(true).`when`(it.authorizationManager).isUserAuthenticated()

            // Call
            it.onLoginPressed()

            // Verify
            verify(it.presenter).login()
        }
    }

    @Test
    fun should_callFirebaseLogin_when_onLoginPressedCalled_with_userNotAuthenticatedLocally() {
        fragmentScenario.onFragment {
            // Stub
            doReturn(false).`when`(it.authorizationManager).isUserAuthenticated()

            // Call
            it.onLoginPressed()

            // Verify
            verify(it.presenter).firebaseLogIn()
        }
    }

    /**
     * Test implementation, manual dependency injection
     */
    class AuthenticationFragmentTestImpl : AuthenticationFragment() {

        override fun injectDependencies() {
            // Mock dependencies
            presenter = mock(AuthenticationMvp.Presenter::class.java)
            authorizationManager = mock(AuthorizationManager::class.java)
            dialogManager = mock(DialogManager::class.java)
            activityContract = mock(ActivityContract::class.java)
        }
    }
}