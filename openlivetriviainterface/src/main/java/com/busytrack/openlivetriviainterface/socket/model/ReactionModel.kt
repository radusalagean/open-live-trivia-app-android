package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class ReactionModel(
    @SerializedName("userId") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("emoji") val emoji: String
)