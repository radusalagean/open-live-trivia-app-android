package com.busytrack.openlivetrivia.generic.mvp

import com.busytrack.openlivetrivia.generic.observer.ReactiveListener
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BasePresenter<T : BaseMvp.View, S : BaseMvp.Model<*>>(
    protected val model: S
) : BaseMvp.Presenter<T>, ReactiveListener {

    override var view: T? = null
    override var refreshing: Boolean = false
        set(value) {
            field = value
            // Update the main progress indicator from the view
            view?.apply { setRefreshingIndicator(value) }
        }

    protected val compositeDisposable = CompositeDisposable()

    override fun dispose() {
        compositeDisposable.clear()
    }

    override fun onError(t: Throwable) {
        Timber.e(t)
        refreshing = false
    }

    override fun onComplete() {
        refreshing = false
    }
}