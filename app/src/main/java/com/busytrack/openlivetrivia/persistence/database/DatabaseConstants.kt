package com.busytrack.openlivetrivia.persistence.database

object DatabaseConstants {
    const val DATABASE_NAME = "main_database"

    object Users {
        const val TABLE_NAME = "users"
        const val COL_ID = "id"
        const val COL_USERNAME = "username"
        const val COL_RIGHTS = "rights"
        const val COL_COINS = "coins"
        const val COL_LAST_SEEN = "last_seen"
        const val COL_JOINED = "joined"
    }
}