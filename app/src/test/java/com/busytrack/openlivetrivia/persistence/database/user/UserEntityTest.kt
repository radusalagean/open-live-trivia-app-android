package com.busytrack.openlivetrivia.persistence.database.user

import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.*

class UserEntityTest {

    private val userEntity = UserEntity(
        userId = "userId",
        username = "username",
        rights = 2,
        coins = 130.0,
        lastSeen = 1570429316986L,
        joined = 1570428936639L
    )

    private val userModel = UserModel(
        userId = "userId",
        username = "username",
        rights = UserRightsLevel.ADMIN,
        coins = 130.0,
        lastSeen = Date(1570429316986),
        playing = false,
        joined = Date(1570428936639)
    )

    @Test
    fun should_returnExpectedUserEntity_when_userModelIsConverted() {
        // Convert UserModel -> UserEntity
        val convertedUserEntity = UserEntity.fromUserModel(userModel)

        // Verify
        assertThat(convertedUserEntity).isEqualTo(userEntity)
    }

    @Test
    fun should_returnExpectedUserModel_when_userEntityIsConverted() {
        // Convert UserEntity -> UserModel
        val convertedUserModel = UserEntity.toUserModel(userEntity)

        // Verify
        assertThat(convertedUserModel).isEqualTo(userModel)
    }
}