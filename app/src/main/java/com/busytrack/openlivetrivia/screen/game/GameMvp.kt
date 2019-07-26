package com.busytrack.openlivetrivia.screen.game

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.*
import io.reactivex.Observable

interface GameMvp {
    interface Model : BaseMvp.Model<GameViewModel> {
        var gameState: GameState?
        var entryReported: Boolean
        fun upgradeToMod(userId: String): Observable<MessageModel>
        fun downgradeToRegular(userId: String): Observable<MessageModel>
    }

    interface View : BaseMvp.View {
        fun onConnecting()
        fun onConnected()
        fun onDisconnected()
        fun onConnectionError()
        fun updateGameState(gameStateModel: GameStateModel)
        fun updateRound(roundModel: RoundModel)
        fun updateSplit(splitModel: SplitModel)
        fun updateAttempt(attemptModel: AttemptModel)
        fun updateReveal(revealModel: RevealModel)
        fun updateCoinDiff(coinDiffModel: CoinDiffModel)
        fun updatePeerJoin(presenceModel: PresenceModel)
        fun updatePeerLeft(presenceModel: PresenceModel)
        fun updatePlayerList(playerListModel: PlayerListModel)
        fun onUserRightsChanged()
    }

    interface Presenter : BaseMvp.Presenter<View> {
        fun initSocketConnection()
        fun disposeSocketConnection()
        fun getGameState(): GameState?
        fun sendAttempt(message: String)
        fun reportEntry()
        fun isEntryReported(): Boolean
        fun requestPlayerList()
        fun upgradeToMod(user: UserModel)
        fun downgradeToRegular(user: UserModel)
    }
}