package com.busytrack.openlivetrivia.view

import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RightsLevelTextViewTest {

    private lateinit var view: RightsLevelTextView

    @Before
    fun init() {
        view = RightsLevelTextView(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun setRightsLevelToRegular_viewIsInCorrectSTate() {
        view.rightsLevel = UserRightsLevel.REGULAR

        assertThat(view.text).isEqualTo("")
        assertThat(view.visibility).isEqualTo(View.INVISIBLE)
    }

    @Test
    fun setRightsLevelToModerator_viewIsInCorrectSTate() {
        view.rightsLevel = UserRightsLevel.MOD

        assertThat(view.text).isEqualTo("MOD")
        assertThat(view.visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun setRightsLevelToAdmin_viewIsInCorrectSTate() {
        view.rightsLevel = UserRightsLevel.ADMIN

        assertThat(view.text).isEqualTo("ADMIN")
        assertThat(view.visibility).isEqualTo(View.VISIBLE)
    }
}