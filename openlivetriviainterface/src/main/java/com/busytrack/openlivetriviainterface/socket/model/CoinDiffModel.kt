package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class CoinDiffModel(
    @SerializedName("coinDiff") val coinDiff: Double
)