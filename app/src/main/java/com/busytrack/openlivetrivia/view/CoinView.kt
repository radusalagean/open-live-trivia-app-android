package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import com.busytrack.openlivetrivia.opengl.renderer.CoinRenderer
import org.rajawali3d.view.ISurface
import org.rajawali3d.view.SurfaceView

class CoinView(context: Context, attributeSet: AttributeSet) : SurfaceView(context, attributeSet) {
    private var renderer: CoinRenderer

    init {
        setFrameRate(COIN_VIEW_FRAMERATE.toDouble())
        renderMode = ISurface.RENDERMODE_CONTINUOUSLY
        mIsTransparent = true
        setSurfaceRenderer(CoinRenderer(context).also { renderer = it })
    }

    fun accelerateLong() {
        renderer.accelerate(COIN_ACCELERATE_LONG)
    }

    fun accelerateShort() {
        renderer.accelerate(COIN_ACCELERATE_SHORT)
    }
}