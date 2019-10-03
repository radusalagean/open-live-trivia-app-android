package com.busytrack.openlivetrivia.generic.fragment

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.generic.activity.BaseActivityImpl
import com.busytrack.openlivetrivia.test.setUpFirebase
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseFragmentTest {

    private lateinit var activityScenario: ActivityScenario<BaseActivityImpl>
    private lateinit var baseFragment: BaseFragmentImpl

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setUp() {
        setUpFirebase()
        activityScenario = ActivityScenario.launch(BaseActivityImpl::class.java)
        activityScenario.onActivity {
            baseFragment = it.getCurrentFragment() as BaseFragmentImpl
        }
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun should_initializeViewModel_when_fragmentIsResumed() {
        verify(baseFragment.mockPresenter, times(1))
            .initViewModel(eq(baseFragment))
    }

    @Test
    fun should_assignViewToPresenter_when_fragmentIsResumed() {
        verify(baseFragment.mockPresenter)
            .view = baseFragment
    }

    @Test
    fun should_removeViewFromPresenter_when_fragmentViewIsDestroyed() {
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        assertThat(baseFragment.mockPresenter.view).isNull()
    }

    @Test
    fun should_callDisposeOnPresenter_when_fragmentIsStopped() {
        activityScenario.moveToState(Lifecycle.State.CREATED)
        verify(baseFragment.mockPresenter, times(1)).dispose()
    }
}