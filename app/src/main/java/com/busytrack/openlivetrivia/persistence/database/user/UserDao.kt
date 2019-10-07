package com.busytrack.openlivetrivia.persistence.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.busytrack.openlivetrivia.persistence.database.DatabaseConstants.Users

/**
 * Data Access Object for the users table.
 */
@Dao
interface UserDao {

    @Query("DELETE FROM ${Users.TABLE_NAME}")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<UserEntity>)

    @Query("SELECT * FROM ${Users.TABLE_NAME}")
    fun getAllUsers(): List<UserEntity>
}