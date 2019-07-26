package com.busytrack.openlivetrivia.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.busytrack.openlivetrivia.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TimedProgressBar(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    private var timerJob: Job? = null

    var progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorAccent)
    }
    private var progress = 0
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