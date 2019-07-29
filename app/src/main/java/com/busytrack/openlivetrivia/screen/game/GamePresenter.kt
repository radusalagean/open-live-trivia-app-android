package com.busytrack.openlivetrivia.screen.game

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetrivia.generic.observer.ReactiveObserver
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.SocketHub
import com.busytrack.openlivetriviainterface.socket.event.SocketEventListener
import com.busytrack.openlivetriviainterface.socket.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class GamePresenter(
    model: GameMvp.Model,
    activityContract: ActivityContract,
    private val socketHub: SocketHub,
    private val authorizationManager: AuthorizationManager
) : BasePresenter<GameMvp.View, GameMvp.Model>(model, activityContract),
    GameMvp.Presenter,
    SocketEventListener,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, GameViewModel::class.java)
    }

    override fun dispose() {
        super.dispose()
        coroutineContext.cancel()
        coroutineContext.cancelChildren()
    }

    override fun initSocketConnection() {
        // Register callbacks on the main thread
        socketHub.registerEventListener(this, Handler(Looper.getMainLooper()))
        socketHub.connect()
    }

    override fun disposeSocketConnection() {
        socketHub.unregisterEventListener(this)
        socketHub.disconnect()
    }

    override fun getGameState(): GameState? {
        return model.gameState
    }

    override fun sendAttempt(message: String) {
        socketHub.attempt(message)
    }

    override fun reportEntry() {
        socketHub.reportCurrentEntry()
    }

    override fun isEntryReported() = model.entryReported

    override fun requestPlayerList() {
        socketHub.requestPlayerList()
    }

    override fun upgradeToMod(user: UserModel) {
        disposer.add(model.upgradeToMod(user.userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<MessageModel>(this) {
                override fun onNext(t: MessageModel) {
                    activityContract.showInfoMessage(
                        R.string.message_user_upgraded_to_moderator,
                        user.username
                    )
                    view?.onUserRightsChanged()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }
            })
        )
    }

    override fun downgradeToRegular(user: UserModel) {
        disposer.add(model.downgradeToRegular(user.userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<MessageModel>(this) {
                override fun onNext(t: MessageModel) {
                    activityContract.showInfoMessage(
                        R.string.message_user_downgraded_to_regular,
                        user.username
                    )
                    view?.onUserRightsChanged()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }
            })
        )
    }

    // Socket Events

    override fun onConnected() {
        launch {
            socketHub.authenticate(async {
                authorizationManager.getIdToken()
            }.await()!!)
        }
    }

    override fun onDisconnected() {
        view?.onDisconnected()
    }

    override fun onConnecting() {
        view?.onConnecting()
    }

    override fun onConnectionError() {
        view?.onConnectionError()
    }

    override fun onConnectionTimeout() {

    }

    override fun onAuthenticated() {
        view?.onConnected()
    }

    override fun onUnauthorized(model: UnauthorizedModel) {

    }

    override fun onWelcome(model: GameStateModel) {
        view?.updateGameState(model)
        this.model.gameState = model.gameState
        this.model.entryReported = model.entryReported
    }

    override fun onPeerJoin(model: PresenceModel) {
        view?.updatePeerJoin(model)
    }

    override fun onPeerAttempt(model: AttemptModel) {
        view?.updateAttempt(model)
        if (model.correct) {
            this.model.gameState = GameState.TRANSITION
        }
    }

    override fun onInsufficientFunds() {
        activityContract.showWarningMessage(R.string.message_insufficient_funds)
    }

    override fun onCoinDiff(model: CoinDiffModel) {
        view?.updateCoinDiff(model)
    }

    override fun onPeerReaction(model: ReactionModel) {
    }

    override fun onRound(model: RoundModel) {
        view?.updateRound(model)
        this.model.gameState = GameState.SPLIT
        this.model.entryReported = false
    }

    override fun onSplit(model: SplitModel) {
        view?.updateSplit(model)
    }

    override fun onReveal(model: RevealModel) {
        view?.updateReveal(model)
        this.model.gameState = GameState.TRANSITION
    }

    override fun onEntryReportedOk() {
        model.entryReported = true
        activityContract.showInfoMessage(R.string.game_entry_reported_successfully)
    }

    override fun onEntryReportedError() {
        activityContract.showErrorMessage(R.string.game_entry_report_error)
    }

    override fun onPlayerList(model: PlayerListModel) {
        view?.updatePlayerList(model)
    }

    override fun onPeerLeft(model: PresenceModel) {
        view?.updatePeerLeft(model)
    }
}