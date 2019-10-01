package com.busytrack.openlivetrivia.generic.observer

import androidx.annotation.CallSuper
import io.reactivex.observers.DisposableObserver

/**
 * A general observer used to handle specific events through a listener
 */
abstract class ReactiveObserver<T>( // TODO test
    private val listener: ReactiveListener
) : DisposableObserver<T>() {

    @CallSuper
    override fun onError(e: Throwable) {
        listener.onError(e)
    }

    //@CallSuper
    override fun onComplete() {
        listener.onComplete()
    }
}