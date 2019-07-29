package com.busytrack.openlivetriviainterface.rest.model

import com.busytrack.openlivetriviainterface.BuildConfig.*
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.google.gson.annotations.SerializedName
import java.util.*

data class UserModel(
    @SerializedName("_id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("rights") val rights: UserRightsLevel?,
    @SerializedName("coins") val coins: Double?,
    @SerializedName("lastSeen") val lastSeen: Date?,
    @SerializedName("playing") val playing: Boolean,
    @SerializedName("joined") val joined: Date?
) {

    companion object {
        fun getThumbnailPath(userId: String) =
            "$ROOT_DOMAIN$PUBLIC_FOLDER_PATH$USER_THUMBNAILS_PATH$userId$USER_THUMBNAILS_EXTENSION"
    }
}