package com.busytrack.openlivetrivia.view

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayersTextViewTest {

    private lateinit var view: PlayersTextView

    @Before
    fun init() {
        view = PlayersTextView(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun setPlayerCount_correctValueIsDisplayed() {
        view.playersCount = 256

        assertThat(view.text).isEqualTo("256")
    }

    @Test
    fun incrementCount_correctValueIsDisplayed() {
        view.playersCount = 256
        view.incrementCount()

        assertThat(view.text).isEqualTo("257")
    }

    @Test
    fun decrementCount_correctValueIsDisplayed() {
        view.playersCount = 256
        view.decrementCount()

        assertThat(view.text).isEqualTo("255")
    }
}