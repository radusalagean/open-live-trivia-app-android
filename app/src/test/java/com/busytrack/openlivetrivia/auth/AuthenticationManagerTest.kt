package com.busytrack.openlivetrivia.auth

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockitokotlin2.inOrder
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class AuthenticationManagerTest {

    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var activityContract: ActivityContract

    private lateinit var authenticationManager: AuthenticationManager

    @Before
    fun setUp() {
        sharedPreferencesRepository = mock(SharedPreferencesRepository::class.java)
        googleSignInClient = spy(GoogleSignIn.getClient(
            ApplicationProvider.getApplicationContext<Context>(),
            mock(GoogleSignInOptions::class.java)
        ))
        firebaseAuth = mock(FirebaseAuth::class.java)
        activityContract = mock(ActivityContract::class.java)
        authenticationManager = AuthenticationManager(
            sharedPreferencesRepository,
            googleSignInClient,
            firebaseAuth,
            activityContract
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun signIn_triggersGoogleSignInFlow() {
        authenticationManager.signIn()

        verify(activityContract).triggerGoogleSignIn(com.nhaarman.mockitokotlin2.any())
    }

    @Test
    fun signOutDefault_authenticatedUserIsSignedOutAndNotified() {
        authenticationManager.signOut()

        verify(firebaseAuth).signOut()
        inOrder(googleSignInClient) {
            verify(googleSignInClient).signOut()
            verify(googleSignInClient).revokeAccess()
        }
        verify(sharedPreferencesRepository).clearAuthenticatedAccount()
        verify(activityContract).handleLogOut()
    }

    @Test
    fun signOutSilent_authenticatedUserIsSignedOutAndNotNotified() {
        authenticationManager.signOut(true)

        verify(firebaseAuth).signOut()
        inOrder(googleSignInClient) {
            verify(googleSignInClient).signOut()
            verify(googleSignInClient).revokeAccess()
        }
        verify(sharedPreferencesRepository).clearAuthenticatedAccount()
        verify(activityContract, never()).handleLogOut()
    }

    @Test
    fun handleGoogleSignInFailure_failureIsHandled() {
        authenticationManager.handleGoogleSignInFailure(-1)

        verify(activityContract).handleFailedFirebaseLogIn(com.nhaarman.mockitokotlin2.any())
    }

    @Test
    fun setAuthenticatedUser_authenticatedUserIsPersisted() {
        val user = mock(UserModel::class.java)
        authenticationManager.setAuthenticatedUser(user)

        verify(sharedPreferencesRepository).updateAuthenticatedAccount(user)
    }

    @Test
    fun getAuthenticatedUser_authenticatedUserIsReturned() {
        val user = mock(UserModel::class.java)
        doReturn(user).`when`(sharedPreferencesRepository).getAuthenticatedAccount()

        val returnedUser = authenticationManager.getAuthenticatedUser()

        assertThat(user).isSameInstanceAs(returnedUser)
    }
}