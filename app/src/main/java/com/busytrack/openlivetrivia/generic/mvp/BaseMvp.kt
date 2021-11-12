package com.busytrack.openlivetrivia.generic.mvp

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import com.busytrack.openlivetrivia.generic.viewmodel.BaseViewModel

interface BaseMvp {

    interface Model<T : BaseViewModel> {
        var viewModel: T
        fun initViewModel(fragment: Fragment, viewModelClass: Class<T>)
    }

    interface View {
        fun setRefreshingIndicator(refreshing: Boolean)
        fun navigate(navDirections: NavDirections)
        fun popBackStack()
    }

    interface Presenter<T : View> {
        var view: T?
        var refreshing: Boolean
        fun dispose()
        fun initViewModel(fragment: Fragment)
    }
}