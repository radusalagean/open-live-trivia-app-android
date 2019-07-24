package com.busytrack.openlivetrivia.extension

import android.widget.EditText

/**
 * Set text to an [EditText] and move the cursor at the end
 */
fun EditText.applyText(text: String) {
    setText(text)
    setSelection(length())
}