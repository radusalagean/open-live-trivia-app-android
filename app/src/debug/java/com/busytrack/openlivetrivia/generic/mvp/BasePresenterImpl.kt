package com.busytrack.openlivetrivia.generic.mvp

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.scheduler.SchedulerProvider
import org.mockito.Mockito.mock

class BasePresenterImpl : BasePresenter<BaseMvp.View, BaseMvp.Model<*>>(
    mock(BaseMvp.Model::class.java),
    mock(ActivityContract::class.java),
    mock(SchedulerProvider::class.java)
) {

    override fun initViewModel(fragment: Fragment) {
    }

    override var idlingResourceInitialized: Boolean = true
}