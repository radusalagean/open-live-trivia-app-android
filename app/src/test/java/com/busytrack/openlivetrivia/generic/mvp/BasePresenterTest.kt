package com.busytrack.openlivetrivia.generic.mvp

import com.busytrack.openlivetrivia.test.EspressoGlobalIdlingResource
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class BasePresenterTest {

    private lateinit var basePresenter: BasePresenterImpl

    @Before
    fun setUp() {
        // Mock EspressoGlobalIdlingResource
        mockkObject(EspressoGlobalIdlingResource)
        // Create the instance under test
        basePresenter = BasePresenterImpl()
        // Set the mock view
        basePresenter.view = mock(BaseMvp.View::class.java)
    }

    @Test
    fun should_registerEspressoGlobalIdlingResource_when_viewIsAssigned() {
        io.mockk.verify {
            EspressoGlobalIdlingResource.registerIdlingResource(basePresenter)
        }
    }

    @Test
    fun should_unregisterEspressoGlobalIdlingResource_when_viewIsRemoved() {
        // Remove the view
        basePresenter.view = null

        // Verify
        io.mockk.verify {
            EspressoGlobalIdlingResource.unregisterIdlingResource(basePresenter)
        }
    }

    @Test
    fun should_updateViewRefreshingIndicator_when_refreshingValueIsChanged() {
        basePresenter.refreshing = true

        verify(basePresenter.view)!!.setRefreshingIndicator(eq(true))
    }

    @Test
    fun should_setRefreshingBackToFalse_when_reactiveListenerCompletes() {
        // Prepare
        basePresenter.refreshing = true

        // Complete subscription
        basePresenter.onComplete()

        // Verify
        assertThat(basePresenter.refreshing).isFalse()
    }

    @Test
    fun should_setRefreshingBackToFalse_when_reactiveListenerErrors() {
        // Prepare
        basePresenter.refreshing = true

        // Complete subscription
        basePresenter.onError(Throwable())

        // Verify
        assertThat(basePresenter.refreshing).isFalse()
    }
}