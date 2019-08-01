package com.busytrack.openlivetrivia.screen.leaderboard

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetrivia.generic.observer.ReactiveObserver
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LeaderboardPresenter(
    model: LeaderboardMvp.Model,
    activityContract: ActivityContract
) : BasePresenter<LeaderboardMvp.View, LeaderboardMvp.Model>(model, activityContract),
    LeaderboardMvp.Presenter {

    private var loadingMoreContent = false
        set(value) {
            field = value
            view?.updateLoadMoreState(value)
        }

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, LeaderboardViewModel::class.java)
    }

    override fun requestLeaderboard(invalidate: Boolean) {
        if (invalidate) {
            model.viewModel.clear()
            disposer.clear()
            loadingMoreContent = false
        }
        model.viewModel.users.let {
            if (it.isNotEmpty()) {
                view?.updateLeaderboard(it)
                return
            }
        }
        refreshing = true
        disposer.add(model.initLeaderboard()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<PaginatedResponseModel<UserModel>>(this) {
                override fun onNext(t: PaginatedResponseModel<UserModel>) {
                    view?.updateLeaderboard(model.viewModel.users)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showWarningMessage(R.string.message_unable_to_refresh_leaderboard)
                    requestCachedLeaderboard()
                }
            }))
    }

    private fun requestCachedLeaderboard() {
        refreshing = true
        disposer.add(model.getCachedLeaderboard()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<List<UserModel>>(this) {
                override fun onNext(t: List<UserModel>) {
                    view?.updateLeaderboard(model.viewModel.users)
                }
            }))
    }

    override fun onScrollThresholdReached() {
        // Load more content
        if (loadingMoreContent || model.viewModel.nextAvailablePage == null) return
        loadingMoreContent = true
        disposer.add(model.getNextLeaderboardPage()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<PaginatedResponseModel<UserModel>>(this) {
                override fun onNext(t: PaginatedResponseModel<UserModel>) {
                    view?.updateLeaderboard(model.viewModel.users)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    loadingMoreContent = false
                }

                override fun onComplete() {
                    super.onComplete()
                    loadingMoreContent = false
                }
            }))
    }

    override fun upgradeToMod(user: UserModel) {
        disposer.add(model.upgradeToMod(user.userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<MessageModel>(this) {
                override fun onNext(t: MessageModel) {
                    activityContract.showInfoMessage(
                        R.string.message_user_upgraded_to_moderator,
                        user.username
                    )
                    view?.onUserRightsChanged()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }
            })
        )
    }

    override fun downgradeToRegular(user: UserModel) {
        disposer.add(model.downgradeToRegular(user.userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<MessageModel>(this) {
                override fun onNext(t: MessageModel) {
                    activityContract.showInfoMessage(
                        R.string.message_user_downgraded_to_regular,
                        user.username
                    )
                    view?.onUserRightsChanged()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }
            })
        )
    }
}