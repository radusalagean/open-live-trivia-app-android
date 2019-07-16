package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.generic.mvp.BasePresenter

class AuthenticationPresenter(
    model: AuthenticationMvp.Model
) : BasePresenter<AuthenticationMvp.View, AuthenticationMvp.Model>(model), AuthenticationMvp.Presenter {

}