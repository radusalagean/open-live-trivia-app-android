package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class OutgoingReactionModel(
    @SerializedName("emoji") val emoji: String
)