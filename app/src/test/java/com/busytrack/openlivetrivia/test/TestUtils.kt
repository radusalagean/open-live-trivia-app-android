package com.busytrack.openlivetrivia.test

import android.os.Build
import org.mockito.internal.util.reflection.FieldSetter
import org.robolectric.util.ReflectionHelpers

/**
 * Set the android SDK version for the current unit test instance
 */
fun setSdkVersion(version: Int) {
    ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", version)
}

/**
 * Assigns the source field to the specified target through reflection
 */
fun assignFieldToTarget(target: Any, targetFieldName: String, source: Any?) {
    FieldSetter.setField(
        target,
        target.javaClass.getDeclaredField(targetFieldName),
        source
    )
}