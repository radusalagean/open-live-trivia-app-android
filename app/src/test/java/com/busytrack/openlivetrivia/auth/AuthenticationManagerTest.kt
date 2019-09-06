package com.busytrack.openlivetrivia.auth

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockitokotlin2.inOrder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule

@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*", "androidx.*", "org.powermock.*")
@PrepareForTest(GoogleSignIn::class)
@RunWith(AndroidJUnit4::class)
class AuthenticationManagerTest {

    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var activityContract: ActivityContract

    private lateinit var authenticationManager: AuthenticationManager

    @get:Rule
    val rule = PowerMockRule()

    @Mock
    lateinit var googleSignInAccountTask: Task<GoogleSignInAccount>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
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

//    @Test
//    fun validIntentPassed_handleGoogleSignInSuccess_continuesWithFirebaseAuthentication() {
//        PowerMockito.mockStatic(GoogleSignIn::class.java)
//        `when`(GoogleSignIn.getSignedInAccountFromIntent(ArgumentMatchers.any(Intent::class.java)))
//            .thenReturn(googleSignInAccountTask)
//
//        authenticationManager.handleGoogleSignInSuccess(mock(Intent::class.java))
//
//        verify(firebaseAuth).signInWithCredential(com.nhaarman.mockitokotlin2.any())
//    }
}