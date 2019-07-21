package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext

private const val FORMAT = "%.2f"
private const val ANIMATION_DELAY = 100L // Milliseconds

class CoinsTextView(
    context: Context,
    attributeSet: AttributeSet
) : TextView(context, attributeSet), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private var animateJob: Job? = null

    var coins: Double? = null
        private set

    fun setCoins(coins: Double) {
        this.coins = coins
        text = FORMAT.format(coins)
    }

    fun computeDiff(diff: Double, millis: Long) {
        Timber.d("computeDiff($diff, $millis)")
//        if (coins == null) { TODO
//            throw IllegalStateException(
//                "Unable to compute diff of uninitialized base value. Assign 'coins' with a value first"
//            )
//        }
//        coins!!.apply {
//            val oldValue = this
//            plus(diff)
//            Timber.d("old $oldValue | new $this")
//            animateDiff(oldValue, this, millis)
//        }
    }

    private fun animateDiff(oldValue: Double, newValue: Double, millis: Long) {
        animateJob?.cancel()
        if (oldValue != newValue && millis > 0) {
            val diff = newValue - oldValue
            val diffPerDrawCycle = diff / (millis / ANIMATION_DELAY)
            var currentValue = oldValue
            animateJob = launch {
                while (currentValue < newValue) {
                    delay(ANIMATION_DELAY)
                    currentValue += diffPerDrawCycle
                    withContext(Dispatchers.Main) {
                        Timber.d("current: $currentValue")
                        text = FORMAT.format(currentValue)
                    }
                }
            }
        }
    }
}