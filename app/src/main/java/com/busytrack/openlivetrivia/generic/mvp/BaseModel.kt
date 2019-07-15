package com.busytrack.openlivetrivia.generic.mvp

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

open class BaseModel<T : ViewModel> : BaseMvp.Model<T> {
    protected lateinit var viewModel: T
        private set

    override fun initViewModel(fragment: Fragment, viewModelClass: Class<T>) {
        viewModel = ViewModelProviders.of(fragment).get(viewModelClass)
    }
}