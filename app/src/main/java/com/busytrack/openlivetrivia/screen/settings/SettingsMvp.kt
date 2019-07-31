package com.busytrack.openlivetrivia.screen.settings

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import io.reactivex.Observable

interface SettingsMvp {

    interface Model : BaseMvp.Model<SettingsViewModel> {
        fun deleteAccount(): Observable<MessageModel>
    }

    interface View : BaseMvp.View

    interface Presenter : BaseMvp.Presenter<View> {
        fun deleteAccount()
    }
}