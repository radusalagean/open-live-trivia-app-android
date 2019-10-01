package com.busytrack.openlivetrivia.screen.mainmenu

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetrivia.generic.observer.ReactiveObserver
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers

class MainMenuPresenter( // TODO test
    model: MainMenuMvp.Model,
    activityContract: ActivityContract,
    private val authenticationManager: AuthenticationManager
) : BasePresenter<MainMenuMvp.View, MainMenuMvp.Model>(model, activityContract), MainMenuMvp.Presenter {

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, MainMenuViewModel::class.java)
    }

    override fun requestMyAccountInfo() {
        disposer.add(model.getMe()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<UserModel>(this) {
                override fun onNext(t: UserModel) {
                    authenticationManager.setAuthenticatedUser(t)
                    view?.updateAccountInfo(t)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showWarningMessage(R.string.message_unable_to_refresh_account_info)
                }
            }))
    }

    // EspressoGlobalIdlingResource implementation

    override var idlingResourceInitialized: Boolean = true
}