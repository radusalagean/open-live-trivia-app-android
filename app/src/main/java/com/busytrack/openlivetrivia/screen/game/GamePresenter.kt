package com.busytrack.openlivetrivia.screen.game

import android.os.Handler
import android.os.Looper
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetriviainterface.socket.SocketHub
import com.busytrack.openlivetriviainterface.socket.event.SocketEventListener
import com.busytrack.openlivetriviainterface.socket.model.*
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class GamePresenter(
    model: GameMvp.Model,
    private val socketHub: SocketHub,
    private val authorizationManager: AuthorizationManager
) : BasePresenter<GameMvp.View, GameMvp.Model>(model),
    GameMvp.Presenter,
    SocketEventListener,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun dispose() {
        super.dispose()
        coroutineContext.cancel()
        coroutineContext.cancelChildren()
        socketHub.unregisterEventListener(this)
    }

    override fun initSocketConnection() {
        // Register callbacks on the main thread
        socketHub.registerEventListener(this, Handler(Looper.getMainLooper()))
        socketHub.connect()
    }

    override fun sendAttempt(message: String) {
        socketHub.attempt(message)
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
    }

    override fun onConnecting() {
    }

    override fun onConnectionError() {
    }

    override fun onConnectionTimeout() {
    }

    override fun onAuthenticated() {
    }

    override fun onUnauthorized(model: UnauthorizedModel) {
    }

    override fun onWelcome(model: GameStateModel) {
        view?.updateGameState(model)
    }

    override fun onPeerJoin(model: PresenceModel) {
        view?.updatePeerJoin(model)
    }

    override fun onPeerAttempt(model: AttemptModel) {
        view?.updateAttempt(model)
    }

    override fun onCoinDiff(model: CoinDiffModel) {
        view?.updateCoinDiff(model)
    }

    override fun onPeerReaction(model: ReactionModel) {
    }

    override fun onRound(model: RoundModel) {
        view?.updateRound(model)
    }

    override fun onSplit(model: SplitModel) {
        view?.updateSplit(model)
    }

    override fun onReveal(model: RevealModel) {
        view?.updateReveal(model)
    }

    override fun onEntryReportedOk() {
    }

    override fun onEntryReportedError() {
    }

    override fun onPlayerList(model: PlayerListModel) {
    }

    override fun onPeerLeft(model: PresenceModel) {
        view?.updatePeerLeft(model)
    }
}