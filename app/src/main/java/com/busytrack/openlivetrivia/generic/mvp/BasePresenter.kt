package com.busytrack.openlivetrivia.generic.mvp

import androidx.test.espresso.IdlingResource
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.observer.ReactiveListener
import com.busytrack.openlivetrivia.generic.scheduler.BaseSchedulerProvider
import com.busytrack.openlivetrivia.generic.scheduler.SchedulerProvider
import com.busytrack.openlivetrivia.test.EspressoIdlingContract
import com.busytrack.openlivetrivia.test.EspressoGlobalIdlingResource
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

abstract class BasePresenter<T : BaseMvp.View, S : BaseMvp.Model<*>>(
    protected val model: S,
    protected val activityContract: ActivityContract,
    protected val schedulerProvider: BaseSchedulerProvider
) : BaseMvp.Presenter<T>, ReactiveListener, EspressoIdlingContract {

    // Mvp Implementation

    override var view: T? = null
        set(value) {
            field = value
            if (value != null) {
                EspressoGlobalIdlingResource.registerIdlingResource(this)
            } else {
                EspressoGlobalIdlingResource.unregisterIdlingResource(this)
            }
        }

    override var refreshing: Boolean = false
        set(value) {
            // Update the main progress indicator from the view
            view?.setRefreshingIndicator(value)
            // Handle blocking event counter
            if (value != field) {
                handleCounterByLoadingState(value)
            }
            // Assign the value to the field
            field = value
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

    // EspressoGlobalIdlingResource implementation

    override val blockingEventCounter: AtomicInteger = AtomicInteger()

    override var resourceCallback: IdlingResource.ResourceCallback? = null
}