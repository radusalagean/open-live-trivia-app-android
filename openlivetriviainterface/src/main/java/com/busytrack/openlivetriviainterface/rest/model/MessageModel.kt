package com.busytrack.openlivetriviainterface.rest.model

import com.google.gson.annotations.SerializedName

data class MessageModel(
    @SerializedName("message") val message: String
)