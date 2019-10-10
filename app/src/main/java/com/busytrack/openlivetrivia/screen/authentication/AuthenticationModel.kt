package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetriviainterface.rest.model.OutgoingRegisterModel
import com.busytrack.openlivetriviainterface.rest.model.SystemInfoModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.Completable
import io.reactivex.Observable

class AuthenticationModel(
    private val networkRepository: NetworkRepository
) : BaseModel<AuthenticationViewModel>(), AuthenticationMvp.Model {

    override fun login(): Observable<UserModel> =
        networkRepository.login()

    override fun registerUser(registerModel: OutgoingRegisterModel): Observable<UserModel> =
        networkRepository.registerUser(registerModel)

    override fun checkUsernameAvailability(username: String): Completable =
        networkRepository.checkUsernameAvailability(username)

    override fun getSystemInfo(): Observable<SystemInfoModel> =
        networkRepository.getSystemInfo()
}