package com.busytrack.openlivetrivia.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.busytrack.openlivetrivia.persistence.database.DatabaseConstants.DATABASE_NAME
import com.busytrack.openlivetrivia.persistence.database.user.UserDao
import com.busytrack.openlivetrivia.persistence.database.user.UserEntity

/**
 * The app database
 */
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}