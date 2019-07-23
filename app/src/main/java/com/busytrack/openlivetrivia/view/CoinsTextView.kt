package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import kotlinx.coroutines.*
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

private const val FORMAT = "%.2f"
private const val ANIMATION_DELAY = 50L // Milliseconds

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
        animateJob?.cancel()
        this.coins = coins
        text = FORMAT.format(coins)
    }

    fun computeDiff(diff: Double, millis: Long) {
        assertCoinsInitialized()
        coins!!.let {
            coins = it.plus(diff)
            animateDiff(it, coins!!, millis)
        }
    }

    fun updateValue(newValue: Double, millis: Long) {
        assertCoinsInitialized()
        coins!!.let {
            coins = newValue
            animateDiff(it, newValue, millis)
        }
    }

    fun drain() {
        updateValue(0.0, COIN_ACCELERATE_LONG)
    }

    private fun assertCoinsInitialized() {
        if (coins == null) {
            throw IllegalStateException(
                "Unable to compute diff of uninitialized base value. Assign 'coins' with a value first"
            )
        }
    }

    private fun animateDiff(oldValue: Double, newValue: Double, millis: Long) {
        animateJob?.cancel()
        if (oldValue != newValue && millis > 0) {
            val diff = newValue - oldValue
            val diffPerDrawCycle = diff / (millis / ANIMATION_DELAY)
            var currentValue = oldValue
            animateJob = launch {
                while (if (diff > 0.0) currentValue < newValue else currentValue > newValue) {
                    delay(ANIMATION_DELAY)
                    currentValue += diffPerDrawCycle
                    withContext(Dispatchers.Main) {
                        text = FORMAT.format(currentValue.absoluteValue)
                    }
                }
            }
        }
    }
}