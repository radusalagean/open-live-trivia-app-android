package com.busytrack.openlivetrivia.screen.settings

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import io.reactivex.Observable

class SettingsModel(
    private val networkRepository: NetworkRepository
) : BaseModel<SettingsViewModel>(), SettingsMvp.Model {

    override fun deleteAccount(): Observable<MessageModel> =
        networkRepository.deleteUser()
}