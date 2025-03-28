package com.busytrack.openlivetrivia.generic.mvp

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.busytrack.openlivetrivia.generic.viewmodel.BaseViewModel

open class BaseModel<T : BaseViewModel> : BaseMvp.Model<T> {
    override lateinit var viewModel: T

    /**
     * Call from the Presenter when the corresponding Fragment is first created
     */
    override fun initViewModel(fragment: Fragment, viewModelClass: Class<T>) {
        viewModel = ViewModelProviders.of(fragment)[viewModelClass]
    }
}