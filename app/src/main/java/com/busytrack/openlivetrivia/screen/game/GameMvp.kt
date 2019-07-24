package com.busytrack.openlivetrivia.screen.game

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetriviainterface.socket.model.*

interface GameMvp {
    interface Model : BaseMvp.Model<GameViewModel> {
        var gameState: GameState?
    }

    interface View : BaseMvp.View {
        fun onConnecting()
        fun onConnected()
        fun onDisconnected()
        fun updateGameState(gameStateModel: GameStateModel)
        fun updateRound(roundModel: RoundModel)
        fun updateSplit(splitModel: SplitModel)
        fun updateAttempt(attemptModel: AttemptModel)
        fun updateReveal(revealModel: RevealModel)
        fun updateCoinDiff(coinDiffModel: CoinDiffModel)
        fun updatePeerJoin(presenceModel: PresenceModel)
        fun updatePeerLeft(presenceModel: PresenceModel)
    }

    interface Presenter : BaseMvp.Presenter<View> {
        fun initSocketConnection()
        fun disposeSocketConnection()
        fun getGameState(): GameState?
        fun sendAttempt(message: String)
    }
}