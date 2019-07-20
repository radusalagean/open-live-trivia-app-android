package com.busytrack.openlivetrivia.opengl.renderer

import android.content.Context
import android.view.MotionEvent
import com.busytrack.openlivetrivia.R
import kotlinx.coroutines.*
import org.rajawali3d.Object3D
import org.rajawali3d.loader.LoaderOBJ
import org.rajawali3d.loader.ParsingException
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.renderer.Renderer
import timber.log.Timber
import org.rajawali3d.lights.DirectionalLight
import kotlin.coroutines.CoroutineContext

const val BASELINE_SPIN_DEGREES = 0.5
const val ACCELERATED_SPIN_DEGREES = 15.0

class CoinRenderer(context: Context) : Renderer(context), CoroutineScope {

    private var coinObject: Object3D? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    var spinDegrees = BASELINE_SPIN_DEGREES
    var spinJob: Job? = null

    init {
        setFrameRate(60)
    }

    override fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int
    ) {
        // Only necessary for live wallpapers
    }

    override fun onTouchEvent(event: MotionEvent?) {
    }

    override fun initScene() {
        addDirectionalLight(1.0, 0.0, -1.0) // Right light
        addDirectionalLight(-1.0, 0.0, -1.0) // Left light
        launch {
            try {
                val objLoader = LoaderOBJ(this@CoinRenderer, R.raw.coin_obj)
                objLoader.parse()
                coinObject = objLoader.parsedObject
                currentScene.addChild(coinObject)
            } catch (pe: ParsingException) {
                Timber.e(pe)
            }
        }
        currentCamera.z = 2.1
    }

    override fun onRender(ellapsedRealtime: Long, deltaTime: Double) {
        super.onRender(ellapsedRealtime, deltaTime)
        coinObject?.rotate(Vector3.Axis.Y, spinDegrees)
    }

    private fun addDirectionalLight(xDir: Double, yDir: Double, zDir: Double) {
        currentScene.addLight(DirectionalLight(xDir, yDir, zDir).apply {
            setColor(1.0f, 1.0f, 1.0f)
            power = .8f
        })
    }

    fun accelerate(millis: Long) {
        spinJob?.cancel()
        spinDegrees = ACCELERATED_SPIN_DEGREES
        spinJob = launch {
            delay(millis)
            spinDegrees = BASELINE_SPIN_DEGREES
            spinJob = null
        }
    }
}