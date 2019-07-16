package com.busytrack.openlivetrivia.generic.mvp

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.busytrack.openlivetrivia.generic.viewmodel.BaseViewModel

open class BaseModel<T : BaseViewModel> : BaseMvp.Model<T> {
    protected lateinit var viewModel: T
        private set

    override fun initViewModel(fragment: Fragment, viewModelClass: Class<T>) {
        viewModel = ViewModelProviders.of(fragment).get(viewModelClass)
    }
}