package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp

interface AuthenticationMvp {
    interface Model : BaseMvp.Model<AuthenticationViewModel> {

    }

    interface View : BaseMvp.View {

    }

    interface Presenter : BaseMvp.Presenter<View> {

    }
}