package com.busytrack.openlivetrivia.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.activity.MainActivity
import com.busytrack.openlivetrivia.application.OpenLiveTriviaApp
import com.busytrack.openlivetrivia.test.ShadowAlertController
import com.busytrack.openlivetrivia.test.ShadowAlertDialog
import com.busytrack.openlivetrivia.test.setUpFirebase
import com.google.common.truth.Truth.assertThat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow


@RunWith(AndroidJUnit4::class)
@Config(
    shadows = [ShadowAlertDialog::class, ShadowAlertController::class]
)
class DialogManagerTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var mockCb: (DialogInterface, Int) -> Unit

    private lateinit var dialogManager: DialogManager

    @Before
    fun setUp() {
        setUpFirebase()
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity {
            dialogManager = DialogManager(it)
        }
        mockCb = mock()
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    // Basic Alert Dialog (with Dismiss button)

    @Test
    fun showBasicAlertDialog_dialogIsShown() {
        dialogManager.showBasicAlertDialog(R.string.test_dialog_title, R.string.test_dialog_message)

        val alertDialog = getLatestAlertDialog()
        assertThat(alertDialog.isShowing).isTrue()
    }

    @Test
    fun showBasicAlertDialog_titleAndMessageAreCorrect() {
        dialogManager.showBasicAlertDialog(R.string.test_dialog_title, R.string.test_dialog_message)

        val shadowAlertDialog = getShadowAlertDialog()
        assertThat(shadowAlertDialog.title).isEqualTo("Title")
        assertThat(shadowAlertDialog.message).isEqualTo("Message")
    }

    @Test
    fun basicAlertDialogIsDisplayed_pressDismissButton_dialogIsNotDisplayedAnymore() {
        dialogManager.showBasicAlertDialog(R.string.test_dialog_title, R.string.test_dialog_message)

        val alertDialog = getLatestAlertDialog()
        val shadowAlertDialog = getShadowAlertDialog()
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).performClick()

        assertThat(shadowAlertDialog.hasBeenDismissed()).isTrue()
    }

    // Alert Dialog (with positive and negative buttons)

    @Test
    fun showAlertDialog_dialogIsShown() {
        dialogManager.showAlertDialog(
            R.string.test_dialog_title,
            R.string.test_dialog_message,
            R.string.test_dialog_positive_button,
            R.string.test_dialog_negative_button,
            mockCb
        )

        val alertDialog = getLatestAlertDialog()
        assertThat(alertDialog.isShowing).isTrue()
    }

    @Test
    fun showAlertDialog_textIsCorrect() {
        dialogManager.showAlertDialog(
            R.string.test_dialog_title,
            R.string.test_dialog_message,
            R.string.test_dialog_positive_button,
            R.string.test_dialog_negative_button,
            mockCb
        )

        val alertDialog = getLatestAlertDialog()
        val shadowAlertDialog = getShadowAlertDialog()
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        assertThat(shadowAlertDialog.title).isEqualTo("Title")
        assertThat(shadowAlertDialog.message).isEqualTo("Message")
        assertThat(positiveButton.text).isEqualTo("Positive")
        assertThat(negativeButton.text).isEqualTo("Negative")
    }

    @Test
    fun alertDialogIsDisplayed_pressPositiveButton_callbackIsReached() {
        val listener = TestDialogClickListener()
        dialogManager.showAlertDialog(
            R.string.test_dialog_title,
            R.string.test_dialog_message,
            R.string.test_dialog_positive_button,
            R.string.test_dialog_negative_button,
            listener::onClick
        )

        val alertDialog = getLatestAlertDialog()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        assertThat(
            listener.clickHistory.filter {
                it == AlertDialog.BUTTON_POSITIVE
            }.size
        ).isEqualTo(1)
    }

    @Test
    fun alertDialogIsDisplayed_pressNegativeButton_dialogIsNotDisplayedAnymore() {
        dialogManager.showAlertDialog(
            R.string.test_dialog_title,
            R.string.test_dialog_message,
            R.string.test_dialog_positive_button,
            R.string.test_dialog_negative_button,
            mockCb
        )

        val alertDialog = getLatestAlertDialog()
        val shadowAlertDialog = getShadowAlertDialog()
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick()

        assertThat(shadowAlertDialog.hasBeenDismissed()).isTrue()
    }

    // List Alert Dialog

    @Test
    fun showListAlertDialog_dialogIsShown() {
        dialogManager.showListAlertDialog(
            R.string.test_dialog_title,
            emptyArray(),
            mockCb
        )

        val alertDialog = getLatestAlertDialog()

        assertThat(alertDialog.isShowing).isTrue()
    }

    @Test
    fun showListAlertDialog_titleAndItemsAreCorrect() {
        val items = arrayOf("Item 1", "Item 2", "Item 3")
        dialogManager.showListAlertDialog(
            R.string.test_dialog_title,
            items,
            mockCb
        )

        val shadowAlertDialog = getShadowAlertDialog()
        val receivedItems = shadowAlertDialog.getItems()!!

        assertThat(shadowAlertDialog.title).isEqualTo("Title")
        assertThat(receivedItems[0]).isEqualTo("Item 1")
        assertThat(receivedItems[1]).isEqualTo("Item 2")
        assertThat(receivedItems[2]).isEqualTo("Item 3")
    }

    @Test
    fun listAlertDialogIsDisplayed_clickOnItems_correctOrder() {
        val items = arrayOf("Item 1", "Item 2", "Item 3")
        val listener = TestDialogClickListener()
        dialogManager.showListAlertDialog(
            R.string.test_dialog_title,
            items,
            listener::onClick
        )

        val shadowAlertDialog = getShadowAlertDialog()
        shadowAlertDialog.run {
            clickOnItem(2)
            clickOnItem(0)
            clickOnItem(1)
        }
        assertThat(listener.clickHistory).containsExactly(2, 0, 1).inOrder()
    }

    // Fatal Alert Dialog (will exit the app when dismissed)

    @Test
    fun showFatalAlertDialog_dialogIsShown() {
        dialogManager.showFatalAlertDialog(
            R.string.test_dialog_title,
            R.string.test_dialog_message
        )

        assertThat(getLatestAlertDialog().isShowing).isTrue()
    }

    @Test
    fun showFatalAlertDialog_titleAndMessageAreCorrect() {
        dialogManager.showFatalAlertDialog(
            R.string.test_dialog_title,
            R.string.test_dialog_message
        )

        val shadowAlertDialog = getShadowAlertDialog()

        assertThat(shadowAlertDialog.title).isEqualTo("Title")
        assertThat(shadowAlertDialog.message).isEqualTo("Message")
    }

    @Test
    fun fatalAlertDialogIsShown_pressDismiss_activityIsFinished() {
        dialogManager.showFatalAlertDialog(
            R.string.test_dialog_title,
            R.string.test_dialog_message
        )

        getLatestAlertDialog().getButton(AlertDialog.BUTTON_NEUTRAL).performClick()

        activityScenario.onActivity {
            assertThat(it.isFinishing).isTrue()
        }
    }

    /**
     * Returns the latest alert dialog
     */
    private fun getLatestAlertDialog(): AlertDialog = ShadowAlertDialog.latestAlertDialog!!

    /**
     * Returns the shadow of the passed alert dialog.
     *
     * By default, it returns the shadow for the latest alert dialog.
     */
    private fun getShadowAlertDialog(alertDialog: AlertDialog? = getLatestAlertDialog()):
            ShadowAlertDialog = Shadow.extract(alertDialog)

    /**
     * Test OnClickListener for [AlertDialog]
     *
     * Keeps history if the click history, which can be used for testing proper functionality
     */
    private class TestDialogClickListener : DialogInterface.OnClickListener {

        val clickHistory = arrayListOf<Int>()

        override fun onClick(dialog: DialogInterface, item: Int) {
            clickHistory.add(item)
        }
    }
}