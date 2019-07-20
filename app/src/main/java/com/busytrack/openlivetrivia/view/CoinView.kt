package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import com.busytrack.openlivetrivia.opengl.renderer.CoinRenderer
import org.rajawali3d.view.ISurface
import org.rajawali3d.view.SurfaceView

class CoinView(context: Context, attributeSet: AttributeSet) : SurfaceView(context, attributeSet) {

    private var renderer: CoinRenderer

    init {
        setFrameRate(60.0)
        renderMode = ISurface.RENDERMODE_WHEN_DIRTY
        mIsTransparent = true
        setSurfaceRenderer(CoinRenderer(context).also { renderer = it })
    }

    fun accelerateLong() {
        renderer.accelerate(2000)
    }

    fun accelerateShort() {
        renderer.accelerate(300)
    }
}