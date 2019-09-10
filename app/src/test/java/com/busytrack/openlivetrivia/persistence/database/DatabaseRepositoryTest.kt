package com.busytrack.openlivetrivia.persistence.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseRepositoryTest {

    lateinit var database: AppDatabase

    lateinit var databaseRepository: DatabaseRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        databaseRepository = DatabaseRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenOneUser_whenInsertUser_thenUserIsPersisted() {
        // Prepare the user
        val user = getTestUserModel("1")

        // Insert the user
        databaseRepository.insertUsers(listOf(user))

        // Check if the user was persisted
        databaseRepository.getAllUsers().test().run {
            assertNoErrors()
            assertValue { it[0] == user }
        }
    }

    @Test
    fun givenPopulatedDatabase_whenUsersAreInserted_thenOldUsersAreUpdatedAndNewOnesAreInserted() {
        // Prepare the initial users
        val initialUsers = listOf(getTestUserModel("1"), getTestUserModel("2"))

        // Prepare the additional users to be inserted
        val additionalUsers = listOf(
            getTestUserModel(userId = "2", rights = UserRightsLevel.REGULAR), // this one should be updated
            getTestUserModel("3")
        )

        // Insert the users in the database
        databaseRepository.run {
            insertUsers(initialUsers)
            insertUsers(additionalUsers)
        }

        // Check the database contents
        databaseRepository.getAllUsers().test().run {
            assertNoErrors()
            assertValue { it[0] == initialUsers[0] }
            assertValue { it[1] == additionalUsers[0] }
            assertValue { it[2] == additionalUsers[1] }
            assertValue { it.size == 3 }
        }
    }

    @Test
    fun givenPopulatedDatabase_whenClearUsers_thenUsersAreCleared() {
        // Prepare the database content
        val user = getTestUserModel("1")
        databaseRepository.insertUsers(listOf(user))

        // Clear users
        databaseRepository.clearUsers()

        // Check if the table is empty
        databaseRepository.getAllUsers().test().run {
            assertNoErrors()
            assertValue { it.isEmpty() }
        }
    }

    private fun getTestUserModel(
        userId: String,
        username: String = "Test Username",
        rights: UserRightsLevel? = UserRightsLevel.MOD,
        coins: Double? = 100.0,
        lastSeen: Date? = Date(0),
        playing: Boolean = false,
        joined: Date? = Date(0)
    ) = UserModel(userId, username, rights, coins, lastSeen, playing, joined)
}