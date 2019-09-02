package com.busytrack.openlivetrivia.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build

/**
 * https://stackoverflow.com/a/51640906/11631823
 */
fun Context.unwrap(): Context? {
    var context: Context = this
    while(context !is Activity && context is ContextWrapper) {
        context = context.baseContext
    }
    return context
}

/**
 * Returns true if the current session is a Robolectric test one, false otherwise
 */
fun Context.isRobolectricUnitTest() = "robolectric" == Build.FINGERPRINT