package com.busytrack.openlivetrivia.persistence.sharedprefs

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.google.common.truth.Truth.assertThat
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class SharedPreferencesRepositoryTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    private val userModel = UserModel(
        userId = "userId",
        username = "username",
        rights = UserRightsLevel.ADMIN,
        coins = 130.0,
        lastSeen = Date(1570429316986),
        playing = false,
        joined = Date(1570428936639)
    )

    @Before
    fun setUp() {
        sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
        sharedPreferencesRepository = SharedPreferencesRepository(sharedPreferences)
        // Insert authenticated user
        sharedPreferencesRepository.updateAuthenticatedAccount(userModel)
    }

    @Test
    fun should_returnExpectedAccount_when_updateAuthenticatedAccountIsCalled() {
        // Query
        val returnedUserModel = sharedPreferencesRepository.getAuthenticatedAccount()!!

        // Check
        assertThat(returnedUserModel.userId).isEqualTo(userModel.userId)
        assertThat(returnedUserModel.username).isEqualTo(userModel.username)
        assertThat(returnedUserModel.rights).isEqualTo(userModel.rights)
        assertThat(returnedUserModel.coins).isEqualTo(userModel.coins)
        assertThat(returnedUserModel.joined).isEqualTo(userModel.joined)
    }

    @Test
    fun should_returnNull_when_getAuthenticatedUserIsCalled_with_noUserPersisted() {
        // Clear
        sharedPreferencesRepository.clearAuthenticatedAccount()

        // Check
        assertThat(sharedPreferencesRepository.getAuthenticatedAccount()).isNull()
    }

    @Test
    fun should_returnExpectedSettingsOptions_when_settingsOptionsArePersisted() {
        // Prepare the options with NON-DEFAULT values
        sharedPreferences.edit()
            .putBoolean(SharedPreferencesConstants.PREF_RELATIVE_TIME, false)
            .putBoolean(SharedPreferencesConstants.PREF_SHOW_RULES_ON_GAME_JOIN, false)
            .putBoolean(SharedPreferencesConstants.PREF_WINNING_SOUNDS, false)
            .putBoolean(SharedPreferencesConstants.PREF_LOSING_SOUNDS, false)
            .putBoolean(SharedPreferencesConstants.PREF_ATTEMPT_SOUNDS, false)
            .putBoolean(SharedPreferencesConstants.PREF_SPLIT_SOUNDS, false)
            .putBoolean(SharedPreferencesConstants.PREF_WINNING_VIBRATION, false)
            .commit()

        // Verify the returned values
        with (sharedPreferencesRepository) {
            assertThat(isRelativeTimeEnabled()).isFalse()
            assertThat(isShowRulesOnGameJoinEnabled()).isFalse()
            assertThat(isWinningSoundsEnabled()).isFalse()
            assertThat(isLosingSoundsEnabled()).isFalse()
            assertThat(isAttemptSoundsEnabled()).isFalse()
            assertThat(isSplitSoundsEnabled()).isFalse()
            assertThat(isWinningVibrationEnabled()).isFalse()
        }
    }
}