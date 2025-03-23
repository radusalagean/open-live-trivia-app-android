package com.busytrack.openlivetrivia.persistence.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.busytrack.openlivetrivia.persistence.database.DatabaseConstants.Users
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import java.util.*

@Entity(tableName = Users.TABLE_NAME)
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = Users.COL_ID) val userId: String,
    @ColumnInfo(name = Users.COL_USERNAME) val username: String,
    @ColumnInfo(name = Users.COL_RIGHTS) val rights: Int,
    @ColumnInfo(name = Users.COL_COINS) val coins: Double,
    @ColumnInfo(name = Users.COL_LAST_SEEN) val lastSeen: Long,
    @ColumnInfo(name = Users.COL_JOINED) val joined: Long
) {

    companion object {
        fun fromUserModel(u: UserModel): UserEntity =
            UserEntity(
                u.userId,
                u.username,
                u.rights!!.ordinal,
                u.coins!!,
                u.lastSeen!!.time,
                u.joined!!.time
            )

        fun toUserModel(u: UserEntity): UserModel =
            UserModel(
                u.userId,
                u.username,
                UserRightsLevel.entries[u.rights],
                u.coins,
                Date(u.lastSeen),
                false,
                Date(u.joined)
            )
    }
}