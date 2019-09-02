package com.busytrack.openlivetrivia.vibration

import android.content.SharedPreferences
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.busytrack.openlivetrivia.setSdkVersion
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class VibrationManagerTest {

    private lateinit var vibrator: Vibrator
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    private lateinit var vibrationManager: VibrationManager

    @Before
    fun setUp() {
        vibrator = mock(Vibrator::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        sharedPreferencesRepository = spy(SharedPreferencesRepository(sharedPreferences))
        vibrationManager = VibrationManager(vibrator, sharedPreferencesRepository)
    }

    @Test
    fun winningVibrationEnabled_equalOrAboveOreo_won_vibrationCommandSent() {
        doReturn(true).`when`(sharedPreferencesRepository).isWinningVibrationEnabled()
        setSdkVersion(26)

        vibrationManager.won()

        verify(vibrator).vibrate(any(VibrationEffect::class.java))
    }

    @Test
    fun winningVibrationEnabled_belowOreo_won_vibrationCommandSent() {
        doReturn(true).`when`(sharedPreferencesRepository).isWinningVibrationEnabled()
        setSdkVersion(25)

        vibrationManager.won()

        verify(vibrator).vibrate(any(LongArray::class.java), anyInt())
    }

    @Test
    fun winningVibrationDisabled_won_vibrationCommandNotSent() {
        doReturn(false).`when`(sharedPreferencesRepository).isWinningVibrationEnabled()

        vibrationManager.won()

        verify(vibrator, never()).vibrate(any(VibrationEffect::class.java)) // SDK 26 or above
        verify(vibrator, never()).vibrate(any(LongArray::class.java), anyInt()) // Below SDK 26
    }
}