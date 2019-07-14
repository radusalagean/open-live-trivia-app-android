package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class PresenceModel(
    @SerializedName("userId") val userId: String,
    @SerializedName("username") val username: String
)