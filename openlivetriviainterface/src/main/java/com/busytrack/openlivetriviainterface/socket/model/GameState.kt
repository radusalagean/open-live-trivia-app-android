package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

enum class GameState {
    @SerializedName("0") NONE,
    @SerializedName("1") SPLIT,
    @SerializedName("2") TRANSITION
}