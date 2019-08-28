package com.busytrack.openlivetrivia.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.busytrack.openlivetrivia.R
import kotlinx.coroutines.*

/**
 * Custom view which displays the graphical representation of the elapsed split time
 */
class TimedProgressBar(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet),
    CoroutineScope {

    override var coroutineContext = Dispatchers.IO

    var timerJob: Job? = null

    private var progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorAccent)
    }
    var progress = 0
        private set
    var max = 1

    override fun onDraw(canvas: Canvas?) {
        val widthUnit = width * progress / max
        canvas?.drawRect(0f, 0f, widthUnit.toFloat(), height.toFloat(), progressPaint)
    }

    private fun startTimer() {
        postInvalidate()
        show()
        stopTimer()
        timerJob = launch {
            if (progress >= max) {
                stopTimer()
                return@launch
            }
            while (isActive && progress < max) {
                delay(1000)
                progress++
                postInvalidate()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun resetAndStart() {
        progress = 0
        startTimer()
    }

    fun setProgressAndStat(progress: Int) {
        this.progress = progress
        startTimer()
    }

    fun hide() {
        visibility = INVISIBLE
    }

    fun show() {
        visibility = VISIBLE
    }
}