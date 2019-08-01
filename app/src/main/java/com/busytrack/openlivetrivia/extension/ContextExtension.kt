package com.busytrack.openlivetrivia.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

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