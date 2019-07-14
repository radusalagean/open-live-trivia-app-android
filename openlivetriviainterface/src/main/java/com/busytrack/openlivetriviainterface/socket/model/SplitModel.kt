package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class SplitModel(
    @SerializedName("answer") val answer: String,
    @SerializedName("currentValue") val currentValue: Double
)