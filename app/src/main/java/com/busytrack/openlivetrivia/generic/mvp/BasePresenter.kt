package com.busytrack.openlivetrivia.generic.mvp

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BaseMvp.View, S : BaseMvp.Model<*>>(
    protected val model: S
) : BaseMvp.Presenter<T> {

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
}