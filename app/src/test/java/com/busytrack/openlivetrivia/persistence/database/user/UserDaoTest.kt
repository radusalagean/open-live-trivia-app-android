package com.busytrack.openlivetrivia.persistence.database.user

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.persistence.database.DaoTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest : DaoTest() {

    // Dummy data
    private val data = listOf(
        UserEntity(
            userId = "userId1",
            username = "username 1",
            rights = 1,
            coins = 100.0,
            lastSeen = 1570428900338L,
            joined = 1570428900338L
        ),
        UserEntity(
            userId = "userId2",
            username = "username 2",
            rights = 2,
            coins = 130.0,
            lastSeen = 1570428936639L,
            joined = 1570428936639L
        )
    )

    @Before
    override fun setUp() {
        super.setUp()
        // Insert initial entities in the database
        db.userDao().insert(data)
    }

    @Test
    fun should_returnNoUsers_when_clearIsCalled_with_populatedTable() {
        // Clear table
        db.userDao().clear()

        // Verify if the table is empty
        val returnedData = db.userDao().getAllUsers()
        assertThat(returnedData).isEmpty()
    }

    @Test
    fun should_returnExpectedItems_when_itemsAreInserted_with_emptyTable() {
        // Verify the returned entities
        val returnedData = db.userDao().getAllUsers()
        assertThat(returnedData).isEqualTo(data)
    }

    @Test
    fun should_replaceOldData_when_updatedItemIsInserted() {
        // Insert updated data for existing user
        val updatedUser = UserEntity(
            userId = "userId2", // Use the same id as the item we want to update (PK)
            username = "updated username 2",
            rights = 0,
            coins = 150.0,
            lastSeen = 1570429316986L,
            joined = 1570428936639L
        )
        db.userDao().insert(listOf(updatedUser))

        // Verify the returned entities
        val returnedData = db.userDao().getAllUsers()
        assertThat(returnedData).isEqualTo(listOf(
            data[0], updatedUser
        ))
    }
}