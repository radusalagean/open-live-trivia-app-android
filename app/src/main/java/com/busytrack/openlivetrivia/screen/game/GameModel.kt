package com.busytrack.openlivetrivia.screen.game

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetriviainterface.socket.model.GameState
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel

class GameModel(
    private val networkRepository: NetworkRepository
) : BaseModel<GameViewModel>(), GameMvp.Model {

    override var gameState: GameState? = null
    override var entryReported = false

    override fun upgradeToMod(userId: String) =
        networkRepository.updateUserRights(userId, UserRightsLevel.MOD.ordinal)

    override fun downgradeToRegular(userId: String) =
        networkRepository.updateUserRights(userId, UserRightsLevel.REGULAR.ordinal)
}