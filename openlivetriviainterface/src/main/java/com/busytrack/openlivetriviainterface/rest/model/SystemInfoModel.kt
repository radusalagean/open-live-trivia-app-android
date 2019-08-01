package com.busytrack.openlivetriviainterface.rest.model

import com.google.gson.annotations.SerializedName

data class SystemInfoModel(
    @SerializedName("serverVersion") val serverVersion: String,
    @SerializedName("minAppVersionCode") val minAppVersionCode: Int,
    @SerializedName("latestAppVersionCode") val latestAppVersionCode: Int,
    @SerializedName("isTriviaServiceRunning") val isTriviaServiceRunning: Boolean
)