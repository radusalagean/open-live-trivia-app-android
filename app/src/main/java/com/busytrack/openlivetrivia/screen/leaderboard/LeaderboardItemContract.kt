package com.busytrack.openlivetrivia.screen.leaderboard

import com.busytrack.openlivetriviainterface.rest.model.UserModel

interface LeaderboardItemContract {
    fun onUserLongClicked(user: UserModel)
}