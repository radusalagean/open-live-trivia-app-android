package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class PlayerModel(
    @SerializedName("_id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("rights") val rights: UserRightsLevel,
    @SerializedName("coins") val coins: Double
)