package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.scheduler.TrampolineSchedulerProvider
import com.busytrack.openlivetriviainterface.rest.model.OutgoingRegisterModel
import com.busytrack.openlivetriviainterface.rest.model.SystemInfoModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.util.*

class AuthenticationPresenterTest {

    @Mock
    private lateinit var model: AuthenticationMvp.Model

    @Mock
    private lateinit var activityContract: ActivityContract

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var authorizationManager: AuthorizationManager

    @Mock
    private lateinit var dialogManager: DialogManager

    private lateinit var authenticationPresenter: AuthenticationPresenter

    private val userModel = UserModel( // Dummy data
        userId = "5d1f2052a93b8d38b87750d3",
        username = "Some user",
        rights = UserRightsLevel.REGULAR,
        coins = 100.0,
        joined = Date(1562323304203)
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        authenticationPresenter = AuthenticationPresenter(
            model, activityContract, TrampolineSchedulerProvider(),
            authenticationManager, authorizationManager, dialogManager
        )
        authenticationPresenter.view = mock()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun should_showFatalAlertDialog_when_checkServerCompatibilityReturnsIncompatibleVersion() {
        // Given
        doReturn(Observable.just(SystemInfoModel(
            serverVersion = "1.0.0",
            minAppVersionCode = Int.MAX_VALUE,
            latestAppVersionCode = Int.MAX_VALUE,
            isTriviaServiceRunning = true
        ))).`when`(model).getSystemInfo()

        // When
        authenticationPresenter.checkServerCompatibility()

        // Then
        verify(dialogManager).showFatalAlertDialog(
            eq(R.string.dialog_title_incompatible_version),
            eq(R.string.dialog_message_incompatible_version)
        )
    }

    @Test
    fun should_showWarningMessage_when_checkServerCompatibilityReturnsOutdatedVersion() {
        // Given
        doReturn(Observable.just(SystemInfoModel(
            serverVersion = "1.0.0",
            minAppVersionCode = Int.MIN_VALUE,
            latestAppVersionCode = Int.MAX_VALUE,
            isTriviaServiceRunning = true
        ))).`when`(model).getSystemInfo()

        // When
        authenticationPresenter.checkServerCompatibility()

        // Then
        verify(activityContract).showWarningMessage(eq(R.string.message_app_update_available))
    }

    @Test
    fun should_showWarningMessage_when_checkServerCompatibilityFails() {
        // Given
        doReturn(Observable.error<SystemInfoModel>(Throwable())).`when`(model).getSystemInfo()

        // When
        authenticationPresenter.checkServerCompatibility()

        // Then
        verify(activityContract).showWarningMessage(
            eq(R.string.message_failed_to_check_server_compatibility)
        )
    }

    @Test
    fun should_signIn_when_firebaseLogInIsCalled() {
        // When
        authenticationPresenter.firebaseLogIn()

        // Then
        verify(authenticationManager).signIn()
    }

    @Test
    fun should_signOutSilently_when_firebaseLogOutIsCalled() {
        // When
        authenticationPresenter.firebaseLogOut()

        // Then
        verify(authenticationManager).signOut(eq(true))
    }

    @Test
    fun should_persistAuthenticatedUserAndShowMainMenuScreen_when_loginReturnsValidResponse() {
        // Given
        doReturn(Observable.just(userModel)).`when`(model).login()

        // When
        authenticationPresenter.login()

        // Then
        inOrder(authenticationManager, activityContract) {
            verify(authenticationManager).setAuthenticatedUser(eq(userModel))
            verify(activityContract).showMainMenuScreen()
        }
    }

    @Test
    fun should_showRegisterPage_when_loginReturns404Error() {
        // Given
        val exception = mock<HttpException>()
        doReturn(HttpURLConnection.HTTP_NOT_FOUND).`when`(exception).code()
        doReturn(Observable.error<UserModel>(exception)).`when`(model).login()

        // When
        authenticationPresenter.login()

        // Then
        verify(authenticationPresenter.view)!!.showRegisterPage()
    }

    @Test
    fun should_showWarningMessageAndMainMenuScreen_when_loginReturnsExceptionAndUserIsLocallyAuthenticated() {
        // Given
        doReturn(Observable.error<UserModel>(Throwable())).`when`(model).login()
        doReturn(mock<UserModel>()).`when`(authenticationManager).getAuthenticatedUser()

        // When
        authenticationPresenter.login()

        // Then
        inOrder(activityContract) {
            verify(activityContract).showWarningMessage(eq(R.string.message_unable_to_authenticate))
            verify(activityContract).showMainMenuScreen()
        }
    }

    @Test
    fun should_showErrorMessage_when_loginReturnsExceptionAndUserIsNotLocallyAuthenticated() {
        // Given
        doReturn(Observable.error<UserModel>(Throwable())).`when`(model).login()

        // When
        authenticationPresenter.login()

        // Then
        verify(activityContract).showErrorMessage(eq(R.string.message_failed_to_log_in), anyOrNull())
        verifyNoMoreInteractions(activityContract)
    }

    @Test
    fun should_persistAuthenticatedUserAndShowMainMenuScreen_when_registerReturnsValidResponse() {
        // Given
        doReturn(Observable.just(userModel)).`when`(model)
            .registerUser(OutgoingRegisterModel(userModel.username))

        // When
        authenticationPresenter.register(userModel.username)

        // Then
        inOrder(authenticationManager, activityContract) {
            verify(authenticationManager).setAuthenticatedUser(eq(userModel))
            verify(activityContract).showInfoMessage(eq(R.string.message_registered))
            verify(activityContract).showMainMenuScreen()
        }
    }

    @Test
    fun should_showErrorMessage_when_registerReturnsException() {
        // Given
        doReturn(Observable.error<UserModel>(Throwable())).`when`(model).registerUser(any())

        // When
        authenticationPresenter.register(userModel.username)

        // Then
        verify(activityContract).showErrorMessage(eq(R.string.general_error_message), anyOrNull())
    }

    @Test
    fun should_updateView_when_checkUsernameAvailabilityCompletes() {
        // Given
        doReturn(Observable.empty<Unit>().ignoreElements())
            .`when`(model).checkUsernameAvailability(any())

        // When
        authenticationPresenter.checkUsernameAvailability("username")

        // Then
        verify(authenticationPresenter.view)!!.setUsernameAvailability(eq(true))
    }

    @Test
    fun should_updateView_when_checkUsernameAvailabilityErrors() {
        // Given
        val exception: HttpException = mock()
        doReturn(HttpURLConnection.HTTP_CONFLICT).`when`(exception).code()
        doReturn(Completable.error(exception))
            .`when`(model).checkUsernameAvailability(any())

        // When
        authenticationPresenter.checkUsernameAvailability("username")

        // Then
        verify(authenticationPresenter.view)!!.setUsernameAvailability(eq(false))
    }
}