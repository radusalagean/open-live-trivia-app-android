package com.busytrack.openlivetrivia.generic.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<BaseFragmentImpl>

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setUp() {
        fragmentScenario = launchFragmentInContainer()
    }

    @After
    fun tearDown() {
        fragmentScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun should_initializeViewModel_when_fragmentIsResumed() {
        fragmentScenario.onFragment {
            verify(it.mockPresenter).initViewModel(eq(it))
        }
    }

    @Test
    fun should_assignViewToPresenter_when_fragmentIsResumed() {
        fragmentScenario.onFragment {
            verify(it.mockPresenter).view = it
        }
    }

    @Test
    fun should_removeViewFromPresenter_when_fragmentViewIsDestroyed() {
        lateinit var fragment: BaseFragmentImpl
        fragmentScenario.onFragment { fragment = it }
        fragmentScenario.moveToState(Lifecycle.State.DESTROYED)
        assertThat(fragment.mockPresenter.view).isNull()
    }

    @Test
    fun should_callDisposeOnPresenter_when_fragmentIsStopped() {
        fragmentScenario.moveToState(Lifecycle.State.CREATED)
        fragmentScenario.onFragment {
            verify(it.mockPresenter).dispose()
        }
    }
}