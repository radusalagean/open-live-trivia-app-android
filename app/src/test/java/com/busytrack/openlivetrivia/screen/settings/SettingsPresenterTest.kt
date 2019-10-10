package com.busytrack.openlivetrivia.screen.settings

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.scheduler.TrampolineSchedulerProvider
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class SettingsPresenterTest {

    @Mock
    private lateinit var model: SettingsMvp.Model

    @Mock
    private lateinit var activityContract: ActivityContract

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    private lateinit var settingsPresenter: SettingsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        settingsPresenter = SettingsPresenter(
            model, activityContract, TrampolineSchedulerProvider(), authenticationManager
        )

    }

    @After
    fun tearDown() {
    }

    @Test
    fun should_signOutAndShowInfoMessage_when_deleteAccountSucceeds() {
        // Given
        doReturn(Observable.just(MessageModel(""))).`when`(model).deleteAccount()

        // When
        settingsPresenter.deleteAccount()

        // Then
        inOrder(authenticationManager, activityContract) {
            verify(authenticationManager).signOut()
            verify(activityContract).showInfoMessage(eq(R.string.message_account_deleted))
        }
    }

    @Test
    fun should_showErrorMessage_when_deleteAccountFails() {
        // Given
        doReturn(Observable.error<MessageModel>(Throwable())).`when`(model).deleteAccount()

        // When
        settingsPresenter.deleteAccount()

        // Then
        verify(activityContract).showErrorMessage(
            eq(R.string.general_error_message), anyOrNull()
        )
    }
}