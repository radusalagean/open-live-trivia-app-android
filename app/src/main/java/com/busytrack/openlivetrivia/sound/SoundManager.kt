package com.busytrack.openlivetrivia.sound

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.VisibleForTesting
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository

class SoundManager(
    context: Context,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    @VisibleForTesting val wonPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.won)
    @VisibleForTesting val lostPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.lost)
    @VisibleForTesting val attemptPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.attempt)
    @VisibleForTesting val splitPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.split)

    fun won() {
        if (sharedPreferencesRepository.isWinningSoundsEnabled()) {
            wonPlayer?.start()
        }
    }

    fun lost() {
        if (sharedPreferencesRepository.isLosingSoundsEnabled()) {
            lostPlayer?.start()
        }
    }

    fun attempt() {
        if (sharedPreferencesRepository.isAttemptSoundsEnabled()) {
            attemptPlayer?.start()
        }
    }

    fun split() {
        if (sharedPreferencesRepository.isSplitSoundsEnabled()) {
            splitPlayer?.start()
        }
    }
}