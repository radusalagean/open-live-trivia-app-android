package com.busytrack.openlivetrivia.screen.settings

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetrivia.generic.observer.ReactiveObserver
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import io.reactivex.android.schedulers.AndroidSchedulers

class SettingsPresenter(
    model: SettingsMvp.Model,
    activityContract: ActivityContract,
    private val authenticationManager: AuthenticationManager
) : BasePresenter<SettingsMvp.View, SettingsMvp.Model>(model, activityContract), SettingsMvp.Presenter {

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, SettingsViewModel::class.java)
    }

    override fun deleteAccount() {
        disposer.add(model.deleteAccount()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ReactiveObserver<MessageModel>(this) {
                override fun onNext(t: MessageModel) {
                    authenticationManager.signOut()
                    activityContract.showInfoMessage(R.string.message_account_deleted)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    activityContract.showErrorMessage(R.string.general_error_message, e.message)
                }
            }))
    }
}