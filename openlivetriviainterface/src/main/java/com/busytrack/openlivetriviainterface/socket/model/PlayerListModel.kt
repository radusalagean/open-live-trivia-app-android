package com.busytrack.openlivetriviainterface.socket.model

import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.google.gson.annotations.SerializedName

data class  PlayerListModel(
    @SerializedName("players")
    val players: List<UserModel>
)