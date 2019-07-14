package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

data class GameStateModel(
    @SerializedName("gameState") val gameState: GameState,
    @SerializedName("userCoins") val userCoins: Double,
    @SerializedName("entryId") val entryId: Int,
    @SerializedName("category") val category: String,
    @SerializedName("clue") val clue: String,
    @SerializedName("answer") val answer: String,
    @SerializedName("currentValue") val currentValue: Double,
    @SerializedName("elapsedSplitSeconds") val elapsedSplitSeconds: Int,
    @SerializedName("totalSplitSeconds") val totalSplitSeconds: Int,
    @SerializedName("freeAttemptsLeft") val freeAttemptsLeft: Int,
    @SerializedName("entryReported") val entryReported: Boolean,
    @SerializedName("players") val players: Int,
    @SerializedName("attempts") val attempts: List<AttemptModel>
)