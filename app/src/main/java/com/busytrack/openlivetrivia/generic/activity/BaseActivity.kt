package com.busytrack.openlivetrivia.generic.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    private val tag : String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(tag).v("-A-> onCreate($savedInstanceState)")
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Timber.tag(tag).v("-A-> onStart()")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Timber.tag(tag).v("-A-> onRestoreInstanceState($savedInstanceState)")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        Timber.tag(tag).v("-A-> onResume()")
        super.onResume()
    }

    override fun onPause() {
        Timber.tag(tag).v("-A-> onPause()")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Timber.tag(tag).v("-A-> onSaveInstanceState($outState)")
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        Timber.tag(tag).v("-A-> onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.tag(tag).v("-A-> onDestroy()")
        super.onDestroy()
    }
}