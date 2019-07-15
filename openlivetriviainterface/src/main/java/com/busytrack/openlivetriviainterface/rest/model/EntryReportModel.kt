package com.busytrack.openlivetriviainterface.rest.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class EntryReportModel(
    @SerializedName("reporters") val reporters: List<UserModel>,
    @SerializedName("banned") val banned: Boolean,
    @SerializedName("_id") val reportId: String,
    @SerializedName("lastReported") val lastReported: Date,
    @SerializedName("entryId") val entryId: Int,
    @SerializedName("category") val category: String?,
    @SerializedName("clue") val clue: String?,
    @SerializedName("answer") val answer: String?
)