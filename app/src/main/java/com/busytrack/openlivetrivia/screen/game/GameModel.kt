package com.busytrack.openlivetrivia.screen.game

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository

class GameModel(
    private val networkRepository: NetworkRepository
) : BaseModel<GameViewModel>(), GameMvp.Model {
}