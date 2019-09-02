package com.busytrack.openlivetrivia.view

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.google.common.truth.Truth.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class UserStatusTextViewTest {

    private lateinit var view: UserStatusTextView

    @Before
    fun init() {
        view = UserStatusTextView(ApplicationProvider.getApplicationContext())
        view.sharedPreferencesRepository = mock(SharedPreferencesRepository::class.java)
    }

    @Test
    fun setPlayingTrue_viewIsInCorrectState() {
        view.setPlaying(true)

        assertThat(view.text).isEqualTo("Playing")
        assertThat(view.currentTextColor).isEqualTo(view.resources.getColor(
            R.color.colorPositive,
            null
        ))
    }

    @Test
    fun relativeTimeEnabled_setPlayingFalse_viewIsInCorrectState() {
        doReturn(true).`when`(view.sharedPreferencesRepository).isRelativeTimeEnabled()

        view.setPlaying(false, 0L)

        org.junit.Assert.assertThat(view.text.toString(), containsString("Last seen: "))
        org.junit.Assert.assertThat(view.text.toString(), not(containsString("1970")))
        assertThat(view.currentTextColor).isEqualTo(view.defaultTextColor.defaultColor)
    }

    @Test
    fun relativeTimeDisabled_setPlayingFalse_viewIsInCorrectState() {
        doReturn(false).`when`(view.sharedPreferencesRepository).isRelativeTimeEnabled()

        view.setPlaying(false, 0L)

        org.junit.Assert.assertThat(view.text.toString(), containsString("Last seen: "))
        org.junit.Assert.assertThat(view.text.toString(), containsString("1970"))
        assertThat(view.currentTextColor).isEqualTo(view.defaultTextColor.defaultColor)
    }
}