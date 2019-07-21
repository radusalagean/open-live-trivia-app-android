package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import com.busytrack.openlivetrivia.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TimedProgressBar(
    context: Context,
    attributeSet: AttributeSet
) : ProgressBar(context, attributeSet, R.style.Widget_AppCompat_ProgressBar_Horizontal),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    private var timerJob: Job? = null

    private fun startTimer() {
        stopTimer()
        timerJob = launch {
            if (progress >= max) {
                stopTimer()
                return@launch
            }
            while (isActive && progress < max) {
                delay(1000)
                progress++
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun reset() {
        progress = 0
        startTimer()
    }

    fun set(max: Int, progress: Int) {
        this.max = max
        this.progress = progress
        startTimer()
    }
}