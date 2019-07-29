package com.busytrack.openlivetrivia.screen.mainmenu

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.Observable

interface MainMenuMvp {
    interface Model : BaseMvp.Model<MainMenuViewModel> {
        fun getMe(): Observable<UserModel>
    }

    interface View : BaseMvp.View {
        fun updateAccountInfo(userModel: UserModel)
    }

    interface Presenter : BaseMvp.Presenter<View> {
        fun requestMyAccountInfo()
    }
}