package com.busytrack.openlivetrivia.persistence.sharedprefs

import android.content.SharedPreferences
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesConstants.PREF_ACCOUNT_COINS
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesConstants.PREF_ACCOUNT_ID
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesConstants.PREF_ACCOUNT_JOINED
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesConstants.PREF_ACCOUNT_RIGHTS
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesConstants.PREF_ACCOUNT_USERNAME
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import java.util.*

class SharedPreferencesRepository(
    private val sharedPreferences: SharedPreferences
) {

    // Authenticated account

    fun updateAuthenticatedAccount(account: UserModel) {
        sharedPreferences.edit()
            .putString(PREF_ACCOUNT_ID, account.userId)
            .putString(PREF_ACCOUNT_USERNAME, account.username)
            .putInt(PREF_ACCOUNT_RIGHTS, account.rights!!.ordinal)
            .putString(PREF_ACCOUNT_COINS, account.coins!!.toString())
            .putLong(PREF_ACCOUNT_JOINED, account.joined!!.time)
            .apply()
    }

    fun clearAuthenticatedAccount() {
        sharedPreferences.edit()
            .remove(PREF_ACCOUNT_ID)
            .remove(PREF_ACCOUNT_USERNAME)
            .remove(PREF_ACCOUNT_RIGHTS)
            .remove(PREF_ACCOUNT_COINS)
            .remove(PREF_ACCOUNT_JOINED)
            .apply()
    }

    fun getAuthenticatedAccount(): UserModel? {
        val userId = sharedPreferences.getString(PREF_ACCOUNT_ID, null) ?: return null
        return UserModel(
            userId,
            sharedPreferences.getString(PREF_ACCOUNT_USERNAME, null)!!,
            UserRightsLevel.entries[sharedPreferences.getInt(PREF_ACCOUNT_RIGHTS, 0)],
            sharedPreferences.getString(PREF_ACCOUNT_COINS, null)!!.toDouble(),
            null,
            false,
            Date(sharedPreferences.getLong(PREF_ACCOUNT_JOINED, 0L))
        )
    }

    // Settings

    fun isRelativeTimeEnabled() =
        sharedPreferences.getBoolean(SharedPreferencesConstants.PREF_RELATIVE_TIME, true)

    fun isShowRulesOnGameJoinEnabled() =
        sharedPreferences.getBoolean(SharedPreferencesConstants.PREF_SHOW_RULES_ON_GAME_JOIN, true)

    fun isWinningSoundsEnabled() =
        sharedPreferences.getBoolean(SharedPreferencesConstants.PREF_WINNING_SOUNDS, true)

    fun isLosingSoundsEnabled() =
        sharedPreferences.getBoolean(SharedPreferencesConstants.PREF_LOSING_SOUNDS, true)

    fun isAttemptSoundsEnabled() =
        sharedPreferences.getBoolean(SharedPreferencesConstants.PREF_ATTEMPT_SOUNDS, true)

    fun isSplitSoundsEnabled() =
        sharedPreferences.getBoolean(SharedPreferencesConstants.PREF_SPLIT_SOUNDS, true)

    fun isWinningVibrationEnabled() =
        sharedPreferences.getBoolean(SharedPreferencesConstants.PREF_WINNING_VIBRATION, true)
}