package com.busytrack.openlivetrivia

import android.os.Build
import org.robolectric.util.ReflectionHelpers

/**
 * Set the android SDK version for the current unit test instance
 */
fun setSdkVersion(version: Int) {
    ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", version)
}