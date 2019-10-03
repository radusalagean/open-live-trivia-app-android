package com.busytrack.openlivetrivia.generic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.activity.BaseActivityImpl
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import org.mockito.Mockito.mock

/**
 * Barebone implementation for [BaseFragment] with mocked dependencies.
 * Used for unit testing purposes.
 *
 * Note: This class is in the debug variant, and not in the test because it is
 * referenced in [BaseActivityImpl]
 */
class BaseFragmentImpl : BaseFragment() {

    @Suppress("UNCHECKED_CAST")
    var mockPresenter: BaseMvp.Presenter<BaseMvp.View> =
        mock(BaseMvp.Presenter::class.java) as BaseMvp.Presenter<BaseMvp.View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_test, container, false)
    }

    override fun initViews() {
    }

    override fun disposeViews() {
    }

    override fun registerListeners() {
    }

    override fun unregisterListeners() {
    }

    override fun loadData() {
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> {
        return mockPresenter as BaseMvp.Presenter<T>
    }

    override fun getInfoBarContainer(): ViewGroup {
        return mock(ViewGroup::class.java)
    }
}