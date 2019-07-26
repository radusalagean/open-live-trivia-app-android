package com.busytrack.openlivetrivia.generic.observer

interface ReactiveListener {
    fun onError(t: Throwable)
    fun onComplete()
}