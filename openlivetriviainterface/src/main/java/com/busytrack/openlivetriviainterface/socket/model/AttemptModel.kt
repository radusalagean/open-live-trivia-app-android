package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class AttemptModel(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("message") val message: String,
    @SerializedName("correct") val correct: Boolean,
    @SerializedName("correctAnswer") val correctAnswer: String
)