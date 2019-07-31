package com.busytrack.openlivetrivia.sound

import android.content.Context
import android.media.MediaPlayer
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository

class SoundManager(
    context: Context,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    private val wonPlayer = MediaPlayer.create(context, R.raw.won)
    private val lostPlayer = MediaPlayer.create(context, R.raw.lost)
    private val attemptPlayer = MediaPlayer.create(context, R.raw.attempt)
    private val splitPlayer = MediaPlayer.create(context, R.raw.split)

    fun won() {
        if (sharedPreferencesRepository.isWinningSoundsEnabled()) {
            wonPlayer.start()
        }
    }

    fun lost() {
        if (sharedPreferencesRepository.isLosingSoundsEnabled()) {
            lostPlayer.start()
        }
    }

    fun attempt() {
        if (sharedPreferencesRepository.isAttemptSoundsEnabled()) {
            attemptPlayer.start()
        }
    }

    fun split() {
        if (sharedPreferencesRepository.isSplitSoundsEnabled()) {
            splitPlayer.start()
        }
    }
}