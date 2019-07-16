package com.busytrack.openlivetrivia.generic.mvp

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.generic.viewmodel.BaseViewModel

interface BaseMvp {

    interface Model<T : BaseViewModel> {
        fun initViewModel(fragment: Fragment, viewModelClass: Class<T>)
    }

    interface View {
        fun setRefreshingIndicator(refreshing: Boolean)
        fun popFragment()
    }

    interface Presenter<T : View> {
        val refreshing: Boolean
        fun takeView(view: T)
        fun dropView()
        fun clearCompositeDisposable()
    }
}