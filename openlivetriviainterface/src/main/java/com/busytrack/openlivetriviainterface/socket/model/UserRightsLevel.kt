package com.busytrack.openlivetriviainterface.socket.model

import com.google.gson.annotations.SerializedName

enum class UserRightsLevel(val rightsLevel: Int) {
    @SerializedName("0") REGULAR(0),
    @SerializedName("1") MODERATOR(1),
    @SerializedName("2") ADMIN(2)
}