package com.busytrack.openlivetrivia.view

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReportersTextViewTest {

    private lateinit var view: ReportersTextView

    @Before
    fun init() {
        view = ReportersTextView(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun setReporters_nullReportersList_noTextIsDisplayed() {
        view.reporters = null

        assertThat(view.text).isEqualTo("")
    }

    @Test
    fun setReporters_emptyReportersList_accountUnavailableIsDisplayed() {
        view.reporters = listOf()

        assertThat(view.text).isEqualTo("Reported by: [account unavailable]")
    }

    @Test
    fun setReporters_oneReporter_correctTextIsDisplayed() {
        view.reporters = listOf("Reporter 1")

        assertThat(view.text).isEqualTo("Reported by: Reporter 1")
    }

    @Test
    fun setReporters_twoReporters_correctTextIsDisplayed() {
        view.reporters = listOf("Reporter 1", "Reporter 2")

        assertThat(view.text).isEqualTo("Reported by: Reporter 1, Reporter 2")
    }

    @Test
    fun setReporters_threeReporters_correctTextIsDisplayed() {
        view.reporters = listOf("Reporter 1", "Reporter 2", "Reporter 3")

        assertThat(view.text).isEqualTo(
            "Reported by: Reporter 1, Reporter 2, Reporter 3"
        )
    }

    @Test
    fun setReporters_fourReporters_correctTextIsDisplayed() {
        view.reporters = listOf("Reporter 1", "Reporter 2", "Reporter 3", "Reporter 4")

        assertThat(view.text).isEqualTo(
            "Reported by: Reporter 1, Reporter 2, Reporter 3 and 1 others"
        )
    }
}