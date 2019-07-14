package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class OutgoingAttemptModel(
    @SerializedName("message") val message: String
)