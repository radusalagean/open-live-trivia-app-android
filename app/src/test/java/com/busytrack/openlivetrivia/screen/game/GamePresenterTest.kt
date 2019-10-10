package com.busytrack.openlivetrivia.screen.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.scheduler.TrampolineSchedulerProvider
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.SocketHub
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.plugins.RxAndroidPlugins
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GamePresenterTest {

    @Mock
    private lateinit var model: GameMvp.Model

    @Mock
    private lateinit var activityContract: ActivityContract

    @Mock
    private lateinit var socketHub: SocketHub

    @Mock
    private lateinit var authorizationManager: AuthorizationManager

    private lateinit var gamePresenter: GamePresenter

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
        gamePresenter = GamePresenter(
            model, activityContract, TrampolineSchedulerProvider(), socketHub, authorizationManager
        )
        gamePresenter.view = mock()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun should_registerAndConnectToSocketHub_when_initSocketConnectionIsCalled() {
        // When
        gamePresenter.initSocketConnection()

        // Then
        inOrder(socketHub) {
            verify(socketHub).registerEventListener(eq(gamePresenter), any())
            verify(socketHub).connect()
        }
    }

    @Test
    fun should_unregisterAndDisconnectToSocketHub_when_disposeSocketConnectionIsCalled() {
        // When
        gamePresenter.disposeSocketConnection()

        // Then
        inOrder(socketHub) {
            verify(socketHub).unregisterEventListener(eq(gamePresenter))
            verify(socketHub).disconnect()
        }
    }

    @Test
    fun should_showInfoMessageAndUpdateView_when_upgradeToModCompletes() {
        // Given
        doReturn(Observable.just(MessageModel(""))).`when`(model).upgradeToMod(any())

        // When
        gamePresenter.upgradeToMod(userModel)

        // Then
        inOrder(activityContract, gamePresenter.view!!) {
            verify(activityContract).showInfoMessage(
                eq(R.string.message_user_upgraded_to_moderator), eq(userModel.username)
            )
            verify(gamePresenter.view)!!.onUserRightsChanged()
        }
    }

    @Test
    fun should_showInfoMessageAndUpdateView_when_downgradeToRegularCompletes() {
        // Given
        doReturn(Observable.just(MessageModel(""))).`when`(model).downgradeToRegular(any())

        // When
        gamePresenter.downgradeToRegular(userModel)

        // Then
        inOrder(activityContract, gamePresenter.view!!) {
            verify(activityContract).showInfoMessage(
                eq(R.string.message_user_downgraded_to_regular), eq(userModel.username)
            )
            verify(gamePresenter.view)!!.onUserRightsChanged()
        }
    }
}