package com.busytrack.openlivetrivia.screen.leaderboard

import com.busytrack.openlivetrivia.generic.viewmodel.BaseViewModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel

class LeaderboardViewModel : BaseViewModel() {

    var users = arrayListOf<UserModel>()
    var nextAvailablePage: Int? = null

    fun clearUsers() { // TODO test
        users.clear()
        nextAvailablePage = null
    }
}