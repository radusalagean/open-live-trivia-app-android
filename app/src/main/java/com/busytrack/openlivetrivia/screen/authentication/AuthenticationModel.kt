package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository

class AuthenticationModel(
    private val networkRepository: NetworkRepository
) : BaseModel<AuthenticationViewModel>(), AuthenticationMvp.Model {

}