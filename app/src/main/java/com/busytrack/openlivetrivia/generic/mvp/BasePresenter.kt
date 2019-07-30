package com.busytrack.openlivetrivia.generic.mvp

import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.observer.ReactiveListener
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BasePresenter<T : BaseMvp.View, S : BaseMvp.Model<*>>(
    protected val model: S,
    protected val activityContract: ActivityContract
) : BaseMvp.Presenter<T>, ReactiveListener {

    // Mvp Implementation

    override var view: T? = null
    override var refreshing: Boolean = false
        set(value) {
            field = value
            // Update the main progress indicator from the view
            view?.setRefreshingIndicator(value)
        }

    protected val disposer = CompositeDisposable()

    override fun dispose() {
        disposer.clear()
    }

    // Reactive listener

    override fun onError(t: Throwable) {
        Timber.e(t)
        refreshing = false
    }

    override fun onComplete() {
        refreshing = false
    }
}