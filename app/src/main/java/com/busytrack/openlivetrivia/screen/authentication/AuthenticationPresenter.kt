package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter

class AuthenticationPresenter(
    model: AuthenticationMvp.Model,
    private val authenticationManager: AuthenticationManager
) : BasePresenter<AuthenticationMvp.View, AuthenticationMvp.Model>(model),
    AuthenticationMvp.Presenter {

    override fun signIn() {
        authenticationManager.signIn()
        refreshing = true
    }

    override fun signOut() {
        authenticationManager.signOut()
    }
}