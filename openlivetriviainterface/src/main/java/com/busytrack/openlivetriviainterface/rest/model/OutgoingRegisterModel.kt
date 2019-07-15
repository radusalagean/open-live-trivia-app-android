package com.busytrack.openlivetriviainterface.rest.model

import com.google.gson.annotations.SerializedName

data class OutgoingRegisterModel(
    @SerializedName("username") val username: String
)