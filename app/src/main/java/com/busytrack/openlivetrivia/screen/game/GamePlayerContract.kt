package com.busytrack.openlivetrivia.screen.game

import com.busytrack.openlivetriviainterface.rest.model.UserModel

interface GamePlayerContract {
    fun onPlayerLongClicked(userModel: UserModel)
}