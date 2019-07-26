package com.busytrack.openlivetriviainterface.socket.event

import com.busytrack.openlivetriviainterface.socket.model.*

interface SocketEventListener {
    fun onConnected()
    fun onDisconnected()
    fun onConnecting()
    fun onConnectionError()
    fun onConnectionTimeout()
    fun onAuthenticated()
    fun onUnauthorized(model: UnauthorizedModel)
    fun onWelcome(model: GameStateModel)
    fun onPeerJoin(model: PresenceModel)
    fun onPeerAttempt(model: AttemptModel)
    fun onInsufficientFunds()
    fun onCoinDiff(model: CoinDiffModel)
    fun onPeerReaction(model: ReactionModel)
    fun onRound(model: RoundModel)
    fun onSplit(model: SplitModel)
    fun onReveal(model: RevealModel)
    fun onEntryReportedOk()
    fun onEntryReportedError()
    fun onPlayerList(model: PlayerListModel)
    fun onPeerLeft(model: PresenceModel)
}