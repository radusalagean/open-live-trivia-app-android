package com.busytrack.openlivetrivia.generic.mvp

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

interface BaseMvp {

    interface Model<T : ViewModel> {
        fun initViewModel(fragment: Fragment, viewModelClass: Class<T>)
    }

    interface View {
        fun setRefreshingIndicator(refreshing: Boolean)
        fun popFragment()
    }

    interface Presenter<T : View> {
        fun takeView(view: T)
        fun dropView()
        fun clearCompositeDisposable()
        fun setRefreshing(refreshing: Boolean)
        fun isRefreshing()
    }
}