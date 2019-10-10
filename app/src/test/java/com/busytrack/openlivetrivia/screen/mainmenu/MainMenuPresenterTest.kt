package com.busytrack.openlivetrivia.screen.mainmenu

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.scheduler.TrampolineSchedulerProvider
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MainMenuPresenterTest {

    @Mock
    private lateinit var model: MainMenuMvp.Model

    @Mock
    private lateinit var view: MainMenuMvp.View

    @Mock
    private lateinit var activityContract: ActivityContract

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    private lateinit var mainMenuPresenter: MainMenuPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainMenuPresenter = MainMenuPresenter(
            model, activityContract, TrampolineSchedulerProvider(), authenticationManager
        )
        mainMenuPresenter.view = view
    }

    @After
    fun tearDown() {
    }

    @Test
    fun should_persistAuthenticatedUserAndUpdateView_when_requestMyAccountInfoSucceeds() {
        // Given
        val userModel = mock<UserModel>()
        doReturn(Observable.just(userModel)).`when`(model).getMe()

        // When
        mainMenuPresenter.requestMyAccountInfo()

        // Then
        inOrder(authenticationManager, view) {
            verify(authenticationManager).setAuthenticatedUser(eq(userModel))
            verify(view).updateAccountInfo(eq(userModel))
        }
    }

    @Test
    fun should_showWarningMessage_when_requestMyAccountInfoFails() {
        // Given
        doReturn(Observable.error<UserModel>(Throwable())).`when`(model).getMe()

        // When
        mainMenuPresenter.requestMyAccountInfo()

        // Then
        verify(activityContract)
            .showWarningMessage(eq(R.string.message_unable_to_refresh_account_info))
    }
}