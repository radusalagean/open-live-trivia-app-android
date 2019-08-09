package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.busytrack.openlivetrivia.extension.cutTo
import kotlinx.coroutines.*
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue
import kotlin.math.sign

const val COIN_DISPLAY_FORMAT = "%.2f"
private const val ANIMATION_DELAY = 50L // Milliseconds

/**
 * Coin-indicator TextView which is able to compute the difference from old and new values and animate the transition
 * between the values
 */
class CoinsTextView(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatTextView(context, attributeSet), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private var animateJob: Job? = null

    var coins: Double? = null
        private set

    fun setCoins(coins: Double) {
        animateJob?.cancel()
        this.coins = coins
        text = COIN_DISPLAY_FORMAT.format(coins)
    }

    fun computeDiff(diff: Double, millis: Long) {
        assertCoinsInitialized()
        coins!!.let {
            coins = it.plus(diff)
            animateDiff(it, coins!!, millis)
        }
    }

    fun updateValue(newValue: Double, millis: Long) {
        //Timber.w("updateValue($newValue, $millis)")
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
        //Timber.w("animateDiff(oldValue = $oldValue, newValue = $newValue, millis = $millis")
        animateJob?.cancel()
        if (oldValue != newValue && millis > 0L) {
            val diff = newValue - oldValue
            //Timber.w("diff = $diff")
            val diffPerDrawCycle = diff / (millis / ANIMATION_DELAY)
            //Timber.w("diffPerDrawCycle = $diffPerDrawCycle")
            var currentValue = oldValue
            animateJob = launch {
                while (
                    if (diff.sign > 0.0) currentValue.cutTo(2) < newValue.cutTo(2)
                        else currentValue.cutTo(2) > newValue.cutTo(2)
                ) {
                    //Timber.w("[draw cycle]: ===")
                    //Timber.w("[draw cycle]: currentValue: $currentValue, newValue = $newValue")
                    delay(ANIMATION_DELAY)
                    currentValue += diffPerDrawCycle
                    //Timber.w("[draw cycle]: currentValue after: $currentValue")
                    withContext(Dispatchers.Main) {
                        text = COIN_DISPLAY_FORMAT.format(currentValue.absoluteValue)
                        //Timber.w("[draw cycle MAIN THREAD]: set text to: $text")
                    }
                }
            }
        }
    }
}