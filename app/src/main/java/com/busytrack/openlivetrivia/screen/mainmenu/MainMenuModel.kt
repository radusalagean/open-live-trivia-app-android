package com.busytrack.openlivetrivia.screen.mainmenu

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.Observable

class MainMenuModel(
    private val networkRepository: NetworkRepository
) : BaseModel<MainMenuViewModel>(), MainMenuMvp.Model { // TODO test

    override fun getMe(): Observable<UserModel> =
        networkRepository.getMe()
}