package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class RoundModel(
    @SerializedName("entryId") val entryId: Int,
    @SerializedName("category") val category: String,
    @SerializedName("clue") val clue: String,
    @SerializedName("answer") val answer: String,
    @SerializedName("currentValue") val currentValue: Double
)