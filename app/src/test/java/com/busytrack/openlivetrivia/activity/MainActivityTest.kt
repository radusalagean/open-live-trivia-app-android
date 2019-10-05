package com.busytrack.openlivetrivia.activity

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationFragment
import com.busytrack.openlivetrivia.test.setUpFirebase
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Mock
    private lateinit var authenticationManager: AuthenticationManager


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setUpFirebase()
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity {
            it.authenticationManager = authenticationManager
        }
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun should_addAuthenticationFragment_when_activityIsCreated() {
        activityScenario.onActivity {
            assertThat(it.getCurrentFragment())
                .isInstanceOf(AuthenticationFragment::class.java)
        }
    }

    @Test
    fun should_handleGoogleSignSuccess_when_triggerGoogleSignInSucceeded() {
        activityScenario.onActivity {
            val responseIntent = prepareGoogleSignInTest(it, success = true)

            verify(it.authenticationManager)
                .handleGoogleSignInSuccess(eq(responseIntent))
            verifyNoMoreInteractions(it.authenticationManager)
        }
    }

    @Test
    fun should_handleGoogleSignInFailure_when_triggerGoogleSignInFailed() {
        activityScenario.onActivity {
            prepareGoogleSignInTest(it, success = false)

            verify(it.authenticationManager)
                .handleGoogleSignInFailure(any())
            verifyNoMoreInteractions(it.authenticationManager)
        }
    }

    /**
     * Prepares the Google sign in tests and returns the response intent
     */
    private fun prepareGoogleSignInTest(mainActivity: MainActivity, success: Boolean): Intent {
        val spy = spy(mainActivity)
        val requestIntent = Intent()
        val responseIntent = Intent()
        val resultCode = if (success) Activity.RESULT_OK else Activity.RESULT_CANCELED

        spy.triggerGoogleSignIn(requestIntent)

        Shadows.shadowOf(spy).receiveResult(
            requestIntent,
            resultCode,
            responseIntent
        )
        return responseIntent
    }
}