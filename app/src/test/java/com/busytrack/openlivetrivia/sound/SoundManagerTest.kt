package com.busytrack.openlivetrivia.sound

import android.media.MediaPlayer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.test.assignFieldToTarget
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class SoundManagerTest {

    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    private lateinit var soundManager: SoundManager

    @Before
    fun setUp() {
        sharedPreferencesRepository = mock(SharedPreferencesRepository::class.java)
        soundManager = SoundManager(
            ApplicationProvider.getApplicationContext(),
            sharedPreferencesRepository
        )
        assignFieldToTarget(soundManager, "wonPlayer", mock(MediaPlayer::class.java))
        assignFieldToTarget(soundManager, "lostPlayer", mock(MediaPlayer::class.java))
        assignFieldToTarget(soundManager, "attemptPlayer", mock(MediaPlayer::class.java))
        assignFieldToTarget(soundManager, "splitPlayer", mock(MediaPlayer::class.java))
    }

    // Won

    @Test
    fun winningSoundsEnabled_won_playbackStarted() {
        doReturn(true).`when`(sharedPreferencesRepository).isWinningSoundsEnabled()

        soundManager.won()

        verify(soundManager.wonPlayer)!!.start()
    }

    @Test
    fun winningSoundsDisabled_won_playbackNotStarted() {
        doReturn(false).`when`(sharedPreferencesRepository).isWinningSoundsEnabled()

        soundManager.won()

        verify(soundManager.wonPlayer, never())!!.start()
    }

    // Lost

    @Test
    fun losingSoundsEnabled_lost_playbackStarted() {
        doReturn(true).`when`(sharedPreferencesRepository).isLosingSoundsEnabled()

        soundManager.lost()

        verify(soundManager.lostPlayer)!!.start()
    }

    @Test
    fun losingSoundsDisabled_lost_playbackNotStarted() {
        doReturn(false).`when`(sharedPreferencesRepository).isLosingSoundsEnabled()

        soundManager.lost()

        verify(soundManager.lostPlayer, never())!!.start()
    }

    // Attempt

    @Test
    fun attemptSoundsEnabled_attempt_playbackStarted() {
        doReturn(true).`when`(sharedPreferencesRepository).isAttemptSoundsEnabled()

        soundManager.attempt()

        verify(soundManager.attemptPlayer)!!.start()
    }

    @Test
    fun attemptSoundsDisabled_attempt_playbackNotStarted() {
        doReturn(false).`when`(sharedPreferencesRepository).isAttemptSoundsEnabled()

        soundManager.attempt()

        verify(soundManager.attemptPlayer, never())!!.start()
    }

    // Split

    @Test
    fun splitSoundsEnabled_split_playbackStarted() {
        doReturn(true).`when`(sharedPreferencesRepository).isSplitSoundsEnabled()

        soundManager.split()

        verify(soundManager.splitPlayer)!!.start()
    }

    @Test
    fun splitSoundsDisabled_split_playbackNotStarted() {
        doReturn(false).`when`(sharedPreferencesRepository).isSplitSoundsEnabled()

        soundManager.split()

        verify(soundManager.splitPlayer, never())!!.start()
    }
}