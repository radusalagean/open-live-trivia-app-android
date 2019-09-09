package com.busytrack.openlivetrivia

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.busytrack.openlivetrivia.activity.MainActivity
import com.busytrack.openlivetrivia.test.EspressoGlobalIdlingResource
import com.busytrack.openlivetrivia.test.EspressoRoundIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GameplayTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoGlobalIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoGlobalIdlingResource)
        activityScenario.close()
    }

    @Test
    fun mainMenu_openGame_typeTwoMessages_checkIfAnswerBoxIsEmpty() {
        onView(withId(R.id.button_play))
            .perform(click())

        onView(withText(R.string.game_dialog_positive_button_rules))
            .perform(click())

        onView(withId(R.id.edit_text_attempt))
            .perform(typeText("this is the first message"))

        IdlingRegistry.getInstance().register(EspressoRoundIdlingResource)

        onView(withId(R.id.button_send_attempt))
            .perform(click())

        onView(withId(R.id.edit_text_attempt))
            .perform(typeText("this is the second message"))

        onView(withId(R.id.button_send_attempt))
            .perform(click())

        onView(withId(R.id.edit_text_attempt)).check(matches(withText("")))

        IdlingRegistry.getInstance().unregister(EspressoRoundIdlingResource)
    }
}