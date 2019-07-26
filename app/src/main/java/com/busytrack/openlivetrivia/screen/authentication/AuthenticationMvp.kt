package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetriviainterface.rest.model.OutgoingRegisterModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.Completable
import io.reactivex.Observable

interface AuthenticationMvp {
    interface Model : BaseMvp.Model<AuthenticationViewModel> {
        fun login(): Observable<UserModel>
        fun registerUser(registerModel: OutgoingRegisterModel): Observable<UserModel>
        fun checkUsernameAvailability(username: String): Completable
    }

    interface View : BaseMvp.View {
        fun showRegisterPage()
        fun setUsernameAvailability(available: Boolean)
    }

    interface Presenter : BaseMvp.Presenter<View> {
        fun firebaseLogIn()
        fun firebaseLogOut()
        fun login()
        fun register(username: String)
        fun checkUsernameAvailability(username: String)
    }
}