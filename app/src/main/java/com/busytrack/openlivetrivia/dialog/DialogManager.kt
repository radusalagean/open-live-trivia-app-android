package com.busytrack.openlivetrivia.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.extension.unwrap
import com.busytrack.openlivetrivia.generic.activity.BaseActivity

class DialogManager(private val context: Context) {
    /**
     * Show a basic alert dialog
     */
    fun showBasicAlertDialog(title: Int, message: Int) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setNeutralButton(R.string.dialog_dismiss) { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    /**
     * Show an alert dialog with a positive and a negative button
     */
    fun showAlertDialog(
        titleResId: Int, messageResId: Int,
        positiveTextResId: Int = R.string.dialog_positive,
        negativeTextResId: Int = R.string.dialog_negative,
        positiveButtonClickListener: (DialogInterface, Int) -> Unit
    ) {
        AlertDialog.Builder(context).apply {
            setTitle(titleResId)
            setMessage(messageResId)
            setPositiveButton(positiveTextResId, positiveButtonClickListener)
            setNegativeButton(negativeTextResId) { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    /**
     * Show an alert dialog with multiple options, displayed as a list
     */
    fun showListAlertDialog(
        title: Int, items: Array<String>,
        onClickListener: (DialogInterface, Int) -> Unit
    ) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setItems(items, onClickListener)
            show()
        }
    }

    /**
     * Show fatal alert dialog (will exit the app when dismissed)
     */
    fun showFatalAlertDialog(
        title: Int,
        message: Int
    ) {
        fun exitActivity() {
            (context.unwrap() as BaseActivity).finish()
        }
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setNeutralButton(R.string.dialog_exit) { _, _ -> exitActivity() }
            setOnDismissListener { exitActivity() }
            show()
        }
    }
}