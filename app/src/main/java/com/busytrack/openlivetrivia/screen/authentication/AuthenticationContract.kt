package com.busytrack.openlivetrivia.screen.authentication

interface AuthenticationContract {
    fun onLoginPressed()
    fun onRegisterPressed(username: String)
    fun onUsernameChanged(username: String)
}