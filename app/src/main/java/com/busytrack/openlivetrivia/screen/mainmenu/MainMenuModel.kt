package com.busytrack.openlivetrivia.screen.mainmenu

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository

class MainMenuModel(
    private val networkRepository: NetworkRepository
) : BaseModel<MainMenuViewModel>(), MainMenuMvp.Model {

}