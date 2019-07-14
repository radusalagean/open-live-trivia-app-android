package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class OutgoingAuthenticationModel(
    @SerializedName("idToken") val idToken: String
)