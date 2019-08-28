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
class TimedProgressBarTest {

    private lateinit var view: TimedProgressBar

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        view = TimedProgressBar(ApplicationProvider.getApplicationContext()).apply {
            coroutineContext = Dispatchers.Main
            max = 3
        }
    }

    @Test
    fun setProgressToZeroAndStart_endsInCorrectState() = mainCoroutineRule.runBlockingTest {
        view.setProgressAndStat(0)
        view.timerJob?.let { joinAll(it) }

        assertThat(view.progress).isEqualTo(3)
    }
}