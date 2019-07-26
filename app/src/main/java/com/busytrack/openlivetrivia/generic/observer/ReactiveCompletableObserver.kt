package com.busytrack.openlivetrivia.generic.observer

import io.reactivex.observers.DisposableCompletableObserver

/**
 * A general completable observer used to handle specific events through a listener
 */
class ReactiveCompletableObserver(
    private val listener: ReactiveListener
) : DisposableCompletableObserver() {

    override fun onError(e: Throwable) {
        listener.onError(e)
    }

    override fun onComplete() {
        listener.onComplete()
    }
}