package com.busytrack.openlivetrivia.screen.moderatereports

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import timber.log.Timber

class ModerateReportsPresenter( // TODO test
    model: ModerateReportsMvp.Model,
    activityContract: ActivityContract
) : BasePresenter<ModerateReportsMvp.View, ModerateReportsMvp.Model>(model, activityContract),
    ModerateReportsMvp.Presenter {

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, ModerateReportsViewModel::class.java)
    }

    override var refreshingReportedEntries: Boolean = false
        set(value) {
            view?.setReportedEntriesRefreshingIndicator(value)
            if (value != field) {
                handleCounterByLoadingState(value)
            }
            field = value
        }

    override var refreshingBannedEntries: Boolean = false
        set(value) {
            view?.setBannedEntriesRefreshingIndicator(value)
            if (value != field) {
                handleCounterByLoadingState(value)
            }
            field = value
        }

    override var loadingMoreReportedEntries: Boolean = false
        set(value) {
            field = value
            view?.updateReportedEntriesLoadMoreState(value)
        }

    override var loadingMoreBannedEntries: Boolean = false
        set(value) {
            field = value
            view?.updateBannedEntriesLoadMoreState(value)
        }

    override fun requestReportedEntries(invalidate: Boolean) {
        if (invalidate) {
            model.viewModel.clearReported()
            //disposer.clear() // TODO
            loadingMoreReportedEntries = false
        }
        model.viewModel.reportedEntries.let {
            if (it.isNotEmpty()) {
                view?.updateReportedEntries(it)
                return
            }
        }
        refreshingReportedEntries = true
        disposer.add(model.initReportedEntries()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<PaginatedResponseModel<EntryReportModel>>() {
                override fun onNext(t: PaginatedResponseModel<EntryReportModel>) {
                    view?.updateReportedEntries(model.viewModel.reportedEntries)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    refreshingReportedEntries = false
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }

                override fun onComplete() {
                    refreshingReportedEntries = false
                }
            })
        )
    }

    override fun requestBannedEntries(invalidate: Boolean) {
        if (invalidate) {
            model.viewModel.clearBanned()
            //disposer.clear() // TODO
            loadingMoreBannedEntries = false
        }
        model.viewModel.bannedEntries.let {
            if (it.isNotEmpty()) {
                view?.updateBannedEntries(it)
                return
            }
        }
        refreshingBannedEntries = true
        disposer.add(model.initBannedEntries()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<PaginatedResponseModel<EntryReportModel>>() {
                override fun onNext(t: PaginatedResponseModel<EntryReportModel>) {
                    view?.updateBannedEntries(model.viewModel.bannedEntries)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    refreshingBannedEntries = false
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }

                override fun onComplete() {
                    refreshingBannedEntries = false
                }
            }))
    }

    override fun onReportedEntriesScrollThresholdReached() {
        if (loadingMoreReportedEntries || model.viewModel.nextAvailablePageReportedEntries == null) return
        loadingMoreReportedEntries = true
        disposer.add(model.getNextReportedEntriesPage()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<PaginatedResponseModel<EntryReportModel>>() {
                override fun onNext(t: PaginatedResponseModel<EntryReportModel>) {
                    view?.updateReportedEntries(model.viewModel.reportedEntries)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    loadingMoreReportedEntries = false
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }

                override fun onComplete() {
                    loadingMoreReportedEntries = false
                }
            }))
    }

    override fun onBannedEntriesScrollThresholdReached() {
        if (loadingMoreBannedEntries || model.viewModel.nextAvailablePageBannedEntries == null) return
        loadingMoreBannedEntries = true
        disposer.add(model.getNextBannedEntriesPage()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<PaginatedResponseModel<EntryReportModel>>() {
                override fun onNext(t: PaginatedResponseModel<EntryReportModel>) {
                    view?.updateBannedEntries(model.viewModel.bannedEntries)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    loadingMoreBannedEntries = false
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }

                override fun onComplete() {
                    loadingMoreBannedEntries = false
                }
            }))
    }

    override fun banEntry(entry: EntryReportModel) {
        disposer.add(model.banEntry(entry.reportId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<MessageModel>() {
                override fun onNext(t: MessageModel) {
                    requestReportedEntries(true)
                    requestBannedEntries(true)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }

                override fun onComplete() {
                    activityContract.showInfoMessage(R.string.message_entry_banned)
                }
            }))
    }

    override fun unbanEntry(entry: EntryReportModel) {
        disposer.add(model.unbanEntry(entry.reportId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<MessageModel>() {
                override fun onNext(t: MessageModel) {
                    requestReportedEntries(true)
                    requestBannedEntries(true)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }

                override fun onComplete() {
                    activityContract.showInfoMessage(R.string.message_entry_unbanned)
                }
            }))
    }

    override fun dismissReport(entry: EntryReportModel) {
        disposer.add(model.dismissReport(entry.reportId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<MessageModel>() {
                override fun onNext(t: MessageModel) {
                    requestReportedEntries(true)
                    requestBannedEntries(true)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }

                override fun onComplete() {
                    activityContract.showInfoMessage(R.string.message_report_dismissed)
                }
            }))
    }

    // EspressoGlobalIdlingResource implementation

    override var idlingResourceInitialized: Boolean = false
}