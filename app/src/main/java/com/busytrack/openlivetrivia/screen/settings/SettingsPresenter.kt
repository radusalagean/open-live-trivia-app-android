package com.busytrack.openlivetrivia.screen.settings

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetrivia.generic.observer.ReactiveObserver
import com.busytrack.openlivetrivia.generic.scheduler.BaseSchedulerProvider
import com.busytrack.openlivetriviainterface.rest.model.MessageModel

class SettingsPresenter(
    model: SettingsMvp.Model,
    activityContract: ActivityContract,
    schedulerProvider: BaseSchedulerProvider,
    private val authenticationManager: AuthenticationManager
) : BasePresenter<SettingsMvp.View, SettingsMvp.Model>(model, activityContract, schedulerProvider),
    SettingsMvp.Presenter {

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, SettingsViewModel::class.java)
    }

    override fun deleteAccount() {
        disposer.add(model.deleteAccount()
            .observeOn(schedulerProvider.main())
            .subscribeWith(object : ReactiveObserver<MessageModel>(this) {
                override fun onNext(t: MessageModel) {
                    authenticationManager.signOut(activityContract = activityContract)
                    activityContract.showInfoMessage(R.string.message_account_deleted)
                    view?.showAuthenticationScreen()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }
            }))
    }

    override fun disconnectEveryone() {
        disposer.add(model.disconnectEveryone()
            .observeOn(schedulerProvider.main())
            .subscribeWith(object : ReactiveObserver<MessageModel>(this) {
                override fun onNext(t: MessageModel) {
                    activityContract.showInfoMessage(R.string.message_disconnect_everyone_success, t.message)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }
            }))
    }

    // EspressoGlobalIdlingResource implementation

    override var idlingResourceInitialized: Boolean = true
}