package com.busytrack.openlivetrivia.test

import android.content.DialogInterface
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.view.LayoutInflater
import android.view.View
import android.widget.Adapter
import android.widget.FrameLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.test.core.app.ApplicationProvider
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.annotation.RealObject
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadow.api.Shadow.directlyOn
import org.robolectric.shadows.ShadowDialog
import org.robolectric.shadows.ShadowListView
import org.robolectric.util.ReflectionHelpers
import java.lang.reflect.InvocationTargetException

/**
 * Robolectric shadow classes ported for compatibility with AndroidX Alert Dialogs
 *
 * from: https://stackoverflow.com/a/56080761/11631823
 */

@Suppress("unused")
@Implements(AlertDialog::class)
open class ShadowAlertDialog : ShadowDialog() {

    @RealObject
    private lateinit var realAlertDialog: AlertDialog

    private val items: Array<CharSequence>? = null
    private val clickListener: DialogInterface.OnClickListener? = null
    private val isMultiItem: Boolean = false
    private val isSingleItem: Boolean = false
    private val multiChoiceClickListener: DialogInterface.OnMultiChoiceClickListener? = null

    private var custom: FrameLayout? = null

    val customView: FrameLayout
        get() = custom ?: FrameLayout(realAlertDialog.context).apply { custom = this }

    val adapter: Adapter?
        get() = shadowAlertController.adapter

    /**
     * @return the message displayed in the dialog
     */
    open val message: CharSequence
        get() = shadowAlertController.getMessage()

    /**
     * @return the view set with [AlertDialog.Builder.setView]
     */
    val view: View?
        get() = shadowAlertController.view

    /**
     * @return the icon set with [AlertDialog.Builder.setIcon]
     */
    val iconId: Int
        get() = shadowAlertController.iconId

    /**
     * @return return the view set with [AlertDialog.Builder.setCustomTitle]
     */
    val customTitleView: View?
        get() = shadowAlertController.customTitleView

    private val shadowAlertController: ShadowAlertController
        get() {
            val alertController = ReflectionHelpers.getField<Any>(realAlertDialog, "mAlert")
            return Shadow.extract<ShadowAlertController>(alertController)
        }

    /**
     * Simulates a click on the `Dialog` item indicated by `index`. Handles both multi- and single-choice dialogs, tracks which items are currently
     * checked and calls listeners appropriately.
     *
     * @param index the index of the item to click on
     */
    fun clickOnItem(index: Int) {
        val shadowListView = Shadow.extract<ShadowListView>(realAlertDialog.listView)
        shadowListView.performItemClick(index)
    }

    override fun getTitle(): CharSequence {
        return shadowAlertController.getTitle()
    }

    /**
     * @return the items that are available to be clicked on
     */
    fun getItems(): Array<CharSequence>? {
        val adapter = shadowAlertController.adapter ?: return null
        return Array(adapter.count) { adapter.getItem(it) as CharSequence }
    }

    public override fun show() {
        super.show()
        latestShadowAlertDialog = this
    }

    @Implements(AlertDialog.Builder::class)
    class ShadowBuilder

    companion object {

        private var latestShadowAlertDialog: ShadowAlertDialog? = null

        /**
         * @return the most recently created `AlertDialog`, or null if none has been created during this test run
         */
        val latestAlertDialog: AlertDialog?
            get() = latestShadowAlertDialog?.realAlertDialog

        /**
         * Resets the tracking of the most recently created `AlertDialog`
         */
        fun reset() {
            latestShadowAlertDialog = null
        }
    }
}

@Suppress("unused")
@Implements(className = ShadowAlertController.clazzName, isInAndroidSdk = false)
class ShadowAlertController {

    companion object {

        const val clazzName = "androidx.appcompat.app.AlertController"
    }

    @RealObject
    private lateinit var realAlertController: Any

    private var title: CharSequence? = null
    private var message: CharSequence? = null

    var view: View? = null
        @Implementation
        set(view) {
            field = view
            directlyOn<Any>(realAlertController, clazzName, "setView", ReflectionHelpers.ClassParameter(View::class.java, view))
        }

    var customTitleView: View? = null
        private set

    var iconId: Int = 0
        private set

    val adapter: Adapter?
        get() = ReflectionHelpers.callInstanceMethod<ListView>(realAlertController, "getListView").adapter

    @Implementation
    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun setTitle(title: CharSequence) {
        this.title = title
        directlyOn<Any>(realAlertController, clazzName, "setTitle", ReflectionHelpers.ClassParameter(CharSequence::class.java, title))
    }

    fun getTitle(): CharSequence = title ?: ""

    @Implementation
    fun setCustomTitle(customTitleView: View) {
        this.customTitleView = customTitleView
        directlyOn<Any>(realAlertController, clazzName, "setCustomTitle", ReflectionHelpers.ClassParameter(View::class.java, customTitleView))
    }

    @Implementation
    fun setMessage(message: CharSequence) {
        this.message = message
        directlyOn<Any>(realAlertController, clazzName, "setMessage", ReflectionHelpers.ClassParameter(CharSequence::class.java, message))
    }

    fun getMessage(): CharSequence = message ?: ""

    @Implementation(minSdk = LOLLIPOP)
    fun setView(resourceId: Int) {
        view = LayoutInflater.from(ApplicationProvider.getApplicationContext()).inflate(resourceId, null)
    }

    @Implementation
    fun setIcon(iconId: Int) {
        this.iconId = iconId
        directlyOn<Any>(realAlertController, clazzName, "setIcon", ReflectionHelpers.ClassParameter(Int::class.java, iconId))
    }
}