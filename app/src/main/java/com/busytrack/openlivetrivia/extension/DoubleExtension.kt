package com.busytrack.openlivetrivia.extension

import kotlin.math.roundToInt

/**
 * Cut to a specific number of decimals
 */
fun Double.cutTo(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (this * multiplier).roundToInt() / multiplier
}