package com.busytrack.openlivetrivia.screen.mainmenu

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp

interface MainMenuMvp {
    interface Model : BaseMvp.Model<MainMenuViewModel> {

    }

    interface View : BaseMvp.View {

    }

    interface Presenter : BaseMvp.Presenter<View> {

    }
}