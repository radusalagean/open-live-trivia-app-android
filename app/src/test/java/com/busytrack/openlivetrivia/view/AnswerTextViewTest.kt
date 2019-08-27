package com.busytrack.openlivetrivia.view

import android.content.res.ColorStateList
import androidx.test.core.app.ApplicationProvider
import com.busytrack.openlivetrivia.R
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AnswerTextViewTest {

    // Subject under test
    private lateinit var view: AnswerTextView

    // Default text color
    private lateinit var defaultTextColor: ColorStateList

    @Before
    fun init() {
        view = AnswerTextView(ApplicationProvider.getApplicationContext(), null)
        defaultTextColor = view.textColors
    }

    @Test
    fun reveal_TextColor() {
        view.reveal()

        assertThat(view.currentTextColor).isEqualTo(view.resources.getColor(
            R.color.colorRevealAnswer,
            null
        ))
    }

    @Test
    fun correct_TextColor() {
        view.correct()

        assertThat(view.currentTextColor).isEqualTo(view.resources.getColor(
            R.color.colorCorrectAnswer,
            null
        ))
    }

    @Test
    fun resetState_TextColor() {
        view.resetState()

        assertThat(view.currentTextColor).isEqualTo(defaultTextColor.defaultColor)
    }
}