package com.busytrack.openlivetrivia.vibration

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository

class VibrationManager(
    private val vibrator: Vibrator,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    private val winPatternTimings = longArrayOf(0, 50, 30, 50, 30, 50)

    fun won() {
        if (sharedPreferencesRepository.isWinningVibrationEnabled()) {
            executeCommand(winPatternTimings)
        }
    }

    private fun executeCommand(timings: LongArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(timings, -1))
        } else {
            vibrator.vibrate(timings, -1)
        }
    }
}