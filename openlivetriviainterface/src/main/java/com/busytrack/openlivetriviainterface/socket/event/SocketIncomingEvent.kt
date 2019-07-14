package com.busytrack.openlivetriviainterface.socket.event

import com.busytrack.openlivetriviainterface.socket.model.*

internal enum class SocketIncomingEvent(
    val lowerCaseEvent: Boolean = false,
    val modelClass: Class<out Any>? = null
) {

    // Socket.io default events
    CONNECT(lowerCaseEvent = true),
    DISCONNECT(lowerCaseEvent = true),
    CONNECTING(lowerCaseEvent = true),
    CONNECT_ERROR(lowerCaseEvent = true),
    CONNECT_TIMEOUT(lowerCaseEvent = true),

    // Custom events
    AUTHENTICATED(lowerCaseEvent = true),
    UNAUTHORIZED(lowerCaseEvent = true, modelClass = UnauthorizedModel::class.java),
    WELCOME(modelClass = GameStateModel::class.java),
    PEER_JOIN(modelClass = PresenceModel::class.java),
    PEER_ATTEMPT(modelClass = AttemptModel::class.java),
    COIN_DIFF(modelClass = CoinDiffModel::class.java),
    PEER_REACTION(modelClass = ReactionModel::class.java),
    ROUND(modelClass = RoundModel::class.java),
    SPLIT(modelClass = SplitModel::class.java),
    REVEAL(modelClass = RevealModel::class.java),
    ENTRY_REPORTED_OK,
    ENTRY_REPORTED_ERROR,
    PLAYER_LIST(modelClass = PlayerListModel::class.java),
    PEER_LEFT(modelClass = PresenceModel::class.java);

    val eventName: String
        get() {
            return if (lowerCaseEvent) name.toLowerCase() else name
        }
}