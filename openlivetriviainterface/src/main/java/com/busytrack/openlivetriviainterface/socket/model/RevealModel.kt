package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class RevealModel(
    @SerializedName("answer") val answer: String
)