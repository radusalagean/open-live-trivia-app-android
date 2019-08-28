package com.busytrack.openlivetrivia.view

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CoinsTextViewTest {

    // Test subject
    private lateinit var view: CoinsTextView

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        view = CoinsTextView(ApplicationProvider.getApplicationContext(), null)
        view.coroutineContext = Dispatchers.Main
    }

    @Test
    fun setCoins_correctValueIsDisplayed() {
        val coins = 4.32
        view.setCoins(coins)

        assertThat(view.text).isEqualTo("4.32")
    }

    @Test
    fun computeDiff_correctValueIsDisplayed() = mainCoroutineRule.runBlockingTest {
        val coins = 4.32
        view.setCoins(coins)
        val diff = -0.32
        val delay = 2500L
        view.computeDiff(diff, delay)
        view.animateJob?.let { joinAll(it) }

        assertThat(view.text).isEqualTo("4.00")
    }

    @Test
    fun updateValue_correctValueIsDisplayed() = mainCoroutineRule.runBlockingTest {
        val coins = 4.32
        view.setCoins(coins)
        val newValue = 6.00
        val delay = 2500L
        view.updateValue(newValue, delay)
        view.animateJob?.let { joinAll(it) }

        assertThat(view.text).isEqualTo("6.00")
    }

    @Test
    fun drain_correctValueIsDisplayed() = mainCoroutineRule.runBlockingTest {
        val coins = 4.32
        view.setCoins(coins)
        view.drain()
        view.animateJob?.let { joinAll(it) }

        assertThat(view.text).isEqualTo("0.00")
    }
}