package com.busytrack.openlivetriviainterface.rest.model

import com.google.gson.annotations.SerializedName

data class SystemInfoModel(
    @SerializedName("serverVersion") val serverVersion: String,
    @SerializedName("minAppVersionCode") val minAppVersionCode: Int,
    @SerializedName("isTriviaServiceRunning") val isTriviaServiceRunning: Boolean
)