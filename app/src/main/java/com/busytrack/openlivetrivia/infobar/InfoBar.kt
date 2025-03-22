package com.busytrack.openlivetrivia.infobar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.ViewInfoBarBinding
import com.busytrack.openlivetrivia.extension.findSuitableParent
import com.busytrack.openlivetrivia.view.InfoBarView
import com.google.android.material.snackbar.BaseTransientBottomBar

const val TYPE_ERROR = 1
const val TYPE_WARN = 2
const val TYPE_INFO = 3

/**
 * A custom info bar, similar in behavior to the Android SnackBar
 */
class InfoBar(
    parent: ViewGroup,
    content: InfoBarView
) : BaseTransientBottomBar<InfoBar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {
        fun make(view: View, string: String, type: Int): InfoBar {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            val binding = ViewInfoBarBinding.inflate(
                LayoutInflater.from(view.context),
                parent,
                false
            )

            // Apply background
            val color = when(type) {
                TYPE_ERROR -> R.color.colorError
                TYPE_WARN -> R.color.colorWarn
                TYPE_INFO -> R.color.colorInfo
                else -> R.color.colorInfo
            }
            if (color != 0) {
                binding.root.setBackgroundColor(color)
            }

            // Set text
            binding.root.binding.infoBarTextView.text = string

            // We create and return our InfoBar
            return InfoBar(
                parent,
                binding.root
            )
        }
    }
}