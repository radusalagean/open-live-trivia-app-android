package com.busytrack.openlivetriviainterface.socket

import android.os.Handler
import com.busytrack.openlivetriviainterface.BuildConfig.*
import com.busytrack.openlivetriviainterface.extension.toJsonObject
import com.busytrack.openlivetriviainterface.socket.event.SocketEventListener
import com.busytrack.openlivetriviainterface.socket.event.SocketIncomingEvent
import com.busytrack.openlivetriviainterface.socket.event.SocketOutgoingEvent
import com.busytrack.openlivetriviainterface.socket.model.*
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber

class SocketHub(
    private val socket: Socket = IO.socket(ROOT_DOMAIN, IO.Options().apply {
        secure = SOCKET_IO_SECURE_CONNECTION
        path = SOCKET_IO_PATH
    })
) {

    private val gson = Gson()
    private val registeredSocketListeners: MutableMap<SocketEventListener, Handler> = hashMapOf()

    init {
        // Register socket events
        SocketIncomingEvent.entries.forEach { event ->
            socket.on(event.eventName) { args ->
                onEventReceived(event, *args)
            }
        }
    }

    /**
     * Handle an incoming socket event
     */
    internal fun onEventReceived(event: SocketIncomingEvent, vararg args: Any) {
        Timber.d("[SOCKET event received] ${event.eventName}")
        if (registeredSocketListeners.isEmpty()) {
            return
        }
        var model: Any? = null
        if (event.modelClass != null) {
            model = gson.fromJson(args[0].toString(), event.modelClass)
        }
        for ((callbacks, handler) in registeredSocketListeners) {
            handler.post {
                with(callbacks) {
                    when(event) {
                        SocketIncomingEvent.CONNECT -> onConnected()
                        SocketIncomingEvent.DISCONNECT -> onDisconnected()
                        SocketIncomingEvent.CONNECTING -> onConnecting()
                        SocketIncomingEvent.CONNECT_ERROR -> onConnectionError()
                        SocketIncomingEvent.CONNECT_TIMEOUT -> onConnectionTimeout()
                        SocketIncomingEvent.AUTHENTICATED -> onAuthenticated()
                        SocketIncomingEvent.UNAUTHORIZED -> onUnauthorized(model as UnauthorizedModel)
                        SocketIncomingEvent.WELCOME -> onWelcome(model as GameStateModel)
                        SocketIncomingEvent.PEER_JOIN -> onPeerJoin(model as PresenceModel)
                        SocketIncomingEvent.PEER_ATTEMPT -> onPeerAttempt(model as AttemptModel)
                        SocketIncomingEvent.INSUFFICIENT_FUNDS -> onInsufficientFunds()
                        SocketIncomingEvent.COIN_DIFF -> onCoinDiff(model as CoinDiffModel)
                        SocketIncomingEvent.PEER_REACTION -> onPeerReaction(model as ReactionModel)
                        SocketIncomingEvent.ROUND -> onRound(model as RoundModel)
                        SocketIncomingEvent.SPLIT -> onSplit(model as SplitModel)
                        SocketIncomingEvent.REVEAL -> onReveal(model as RevealModel)
                        SocketIncomingEvent.ENTRY_REPORTED_OK -> onEntryReportedOk()
                        SocketIncomingEvent.ENTRY_REPORTED_ERROR -> onEntryReportedError()
                        SocketIncomingEvent.PLAYER_LIST -> onPlayerList(model as PlayerListModel)
                        SocketIncomingEvent.PEER_LEFT -> onPeerLeft(model as PresenceModel)
                    }
                }
            }
        }
    }

    /**
     * Register an event listener, when the Activity / Fragment is started
     */
    fun registerEventListener(listener: SocketEventListener, handler: Handler) {
        Timber.d("registerEventListener")
        registeredSocketListeners[listener] = handler
    }

    /**
     * Unregister an event listener, when the Activity / Fragment is stopped
     */
    fun unregisterEventListener(listener: SocketEventListener) {
        Timber.d("unregisterEventListener")
        registeredSocketListeners.remove(listener)
    }

    fun clearEventListeners() {
        registeredSocketListeners.clear()
    }

    /**
     * Connect to the server, when the Activity / Fragment is started
     */
    fun connect() {
        socket.connect()
    }

    /**
     * Disconnect from the server, when the Activity / Fragment is stopped
     */
    fun disconnect() {
        socket.disconnect()
        socket.close()
    }

    /**
     * As soon as the app is connected to the server, authenticate
     */
    fun authenticate(idToken: String) {
        val model = OutgoingAuthenticationModel(idToken)
        socket.emit(SocketOutgoingEvent.AUTHENTICATION.eventName, gson.toJsonObject(model))
    }

    fun attempt(message: String) {
        val model = OutgoingAttemptModel(message)
        socket.emit(SocketOutgoingEvent.ATTEMPT.eventName, gson.toJsonObject(model))
    }

    fun react(emoji: String) {
        val model = OutgoingReactionModel(emoji)
        socket.emit(SocketOutgoingEvent.REACTION.eventName, gson.toJsonObject(model))
    }

    fun reportCurrentEntry() {
        socket.emit(SocketOutgoingEvent.REPORT_ENTRY.eventName)
    }

    fun requestPlayerList() {
        socket.emit(SocketOutgoingEvent.REQUEST_PLAYER_LIST.eventName)
    }
}