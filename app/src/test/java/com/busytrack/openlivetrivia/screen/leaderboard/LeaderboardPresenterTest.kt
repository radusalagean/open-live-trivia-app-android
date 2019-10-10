package com.busytrack.openlivetrivia.screen.leaderboard

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.scheduler.TrampolineSchedulerProvider
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
class LeaderboardPresenterTest {

    @Mock
    private lateinit var model: LeaderboardMvp.Model

    @Mock
    private lateinit var activityContract: ActivityContract

    @Mock
    private lateinit var viewModel: LeaderboardViewModel

    private lateinit var leaderboardPresenter: LeaderboardPresenter

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
        leaderboardPresenter = LeaderboardPresenter(
            model, activityContract, TrampolineSchedulerProvider()
        )
        leaderboardPresenter.view = mock()
        doReturn(viewModel).`when`(model).viewModel
    }

    @After
    fun tearDown() {
    }

    @Test
    fun should_clearUsersFromViewModel_when_requestLeaderboardWithInvalidateOption() {
        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<*>>()))
            .`when`(model).initLeaderboard()

        // When
        leaderboardPresenter.requestLeaderboard(true)

        // Then
        verify(model.viewModel).clearUsers()
    }

    @Test
    fun should_updateViewWithExistingUserList_when_requestLeaderboardWithExistingUsersInViewModel() {
        // Given
        val users = mock<ArrayList<UserModel>>()
        doReturn(users).`when`(viewModel).users

        // When
        leaderboardPresenter.requestLeaderboard()

        // Then
        verify(leaderboardPresenter.view)!!.updateLeaderboard(eq(users))
    }

    @Test
    fun should_updateViewWithReceivedUserList_when_requestLeaderboardWithNoUsersInViewModel() {
        // Given
        val response = mock<PaginatedResponseModel<UserModel>>()
        doReturn(Observable.just(response)).`when`(model).initLeaderboard()

        // When
        leaderboardPresenter.requestLeaderboard()

        // Then
        verify(leaderboardPresenter.view)!!.updateLeaderboard(any())
    }

    @Test
    fun should_showWarningMessageAndRequestCachedLeaderboard_when_requestLeaderboardErrors() {
        // Given
        doReturn(Observable.error<PaginatedResponseModel<UserModel>>(Throwable()))
            .`when`(model).initLeaderboard()
        doReturn(Observable.just(mock<List<UserModel>>())).`when`(model).getCachedLeaderboard()

        // When
        leaderboardPresenter.requestLeaderboard()

        // Then
        verify(activityContract).showWarningMessage(
            eq(R.string.message_unable_to_refresh_leaderboard)
        )
        verify(leaderboardPresenter.view)!!.updateLeaderboard(eq(viewModel.users))
    }

    @Test
    fun should_ignoreRequest_when_onScrollThresholdReachedAndIsCurrentlyLoadingContent() {
        // Given
        // Subscribe on Schedulers.io() and place a delay for the first call
        // This will be enough so when the second attempt is reached, the first one is still
        //  processing and there should be only one call to model.getNextLeaderboardPage(),
        //  because the second attempt should be ignored when the first one is still processing
        doReturn(
            Observable.just(mock<PaginatedResponseModel<UserModel>>())
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
        ).`when`(model).getNextLeaderboardPage()
        doReturn(2).`when`(viewModel).nextAvailablePage

        // When
        leaderboardPresenter.onScrollThresholdReached() // Attempt 1

        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<UserModel>>()))
            .`when`(model).getNextLeaderboardPage()

        // When
        leaderboardPresenter.onScrollThresholdReached() // Attempt 2

        // Then
        verify(model).getNextLeaderboardPage()
    }

    @Test
    fun should_ignoreRequest_when_onScrollThresholdReachedAndNextPageIsNotAvailable() {
        // Given
        doReturn(null).`when`(viewModel).nextAvailablePage

        // When
        leaderboardPresenter.onScrollThresholdReached()

        // Then
        verify(model, never()).getNextLeaderboardPage() // No request should be made
    }

    @Test
    fun should_showInfoMessageAndUpdateView_when_upgradeToModSucceeds() {
        // Given
        doReturn(Observable.just(MessageModel(""))).`when`(model).upgradeToMod(any())

        // When
        leaderboardPresenter.upgradeToMod(userModel)

        // Then
        verify(activityContract).showInfoMessage(
            eq(R.string.message_user_upgraded_to_moderator), eq(userModel.username)
        )
        verify(leaderboardPresenter.view)!!.onUserRightsChanged()
    }

    @Test
    fun should_showErrorMessage_when_upgradeToModFails() {
        // Given
        doReturn(Observable.error<MessageModel>(Throwable())).`when`(model).upgradeToMod(any())

        // When
        leaderboardPresenter.upgradeToMod(userModel)

        // Then
        verify(activityContract).showErrorMessage(
            eq(R.string.general_error_message), anyOrNull()
        )
    }

    @Test
    fun should_showInfoMessageAndUpdateView_when_downgradeToRegularSucceeds() {
        // Given
        doReturn(Observable.just(MessageModel(""))).`when`(model).downgradeToRegular(any())

        // When
        leaderboardPresenter.downgradeToRegular(userModel)

        // Then
        verify(activityContract).showInfoMessage(
            eq(R.string.message_user_downgraded_to_regular), eq(userModel.username)
        )
        verify(leaderboardPresenter.view)!!.onUserRightsChanged()
    }

    @Test
    fun should_showErrorMessage_when_downgradeToRegularFails() {
        // Given
        doReturn(Observable.error<MessageModel>(Throwable())).`when`(model).downgradeToRegular(any())

        // When
        leaderboardPresenter.downgradeToRegular(userModel)

        // Then
        verify(activityContract).showErrorMessage(
            eq(R.string.general_error_message), anyOrNull()
        )
    }
}