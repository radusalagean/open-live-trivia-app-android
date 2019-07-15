package com.busytrack.openlivetrivia.generic.viewmodel

import androidx.lifecycle.ViewModel
import timber.log.Timber

open class BaseViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared()")
    }
}