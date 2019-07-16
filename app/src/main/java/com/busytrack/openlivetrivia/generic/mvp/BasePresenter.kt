package com.busytrack.openlivetrivia.generic.mvp

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BaseMvp.View, S : BaseMvp.Model<*>>(
    protected val model: S
) : BaseMvp.Presenter<T> {

    protected var view: T? = null
    protected val compositeDisposable = CompositeDisposable()
    override val refreshing: Boolean = false

    override fun takeView(view: T) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }
}