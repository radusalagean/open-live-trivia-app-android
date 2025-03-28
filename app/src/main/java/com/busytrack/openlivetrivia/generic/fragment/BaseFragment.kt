package com.busytrack.openlivetrivia.generic.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp.Presenter
import timber.log.Timber

abstract class BaseFragment<BINDING : ViewBinding> : Fragment(), BaseMvp.View {
    private val logTag = javaClass.simpleName

    private var _binding: BINDING? = null
    val binding: BINDING
        get() = _binding!!

    // Lifecycle callbacks

    override fun onAttach(context: Context) {
        Timber.tag(logTag).d("-F-> onAttach($context)")
        injectDependencies()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(logTag).d("-F-> onCreate($savedInstanceState)")
        super.onCreate(savedInstanceState)
        // Init view model
        getPresenter<BaseFragment<*>>().initViewModel(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.tag(logTag).d("-F-> onCreateView($inflater, $container, $savedInstanceState)")
        _binding = inflateLayout(container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.tag(logTag).d("-F-> onViewCreated($view, $savedInstanceState)")
        super.onViewCreated(view, savedInstanceState)
        initViews()
        registerListeners()
        getPresenter<BaseFragment<*>>().view = this
        loadData()
        savedInstanceState?.let { restoreInstanceState(it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Timber.tag(logTag).d("-F-> onActivityCreated($savedInstanceState)")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Timber.tag(logTag).d("-F-> onStart()")
        super.onStart()
    }

    override fun onResume() {
        Timber.tag(logTag).d("-F-> onResume()")
        super.onResume()
        setRefreshingIndicator(getPresenter<BaseFragment<*>>().refreshing)
    }

    override fun onPause() {
        Timber.tag(logTag).d("-F-> onPause()")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.tag(logTag).d("-F-> onSaveInstanceState()")
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        Timber.tag(logTag).d("-F-> onStop()")
        getPresenter<BaseFragment<*>>().dispose()
        super.onStop()
    }

    override fun onDestroyView() {
        Timber.tag(logTag).d("-F-> onDestroyView()")
        getPresenter<BaseFragment<*>>().view = null
        unregisterListeners()
        disposeViews()
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Timber.tag(logTag).d("-F-> onDestroy()")
        super.onDestroy()
    }

    override fun onDetach() {
        Timber.tag(logTag).d("-F-> onDetach()")
        super.onDetach()
    }

    // Mvp Implementation

    override fun setRefreshingIndicator(refreshing: Boolean) {
        // Empty implementation
    }

    override fun navigate(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    override fun popBackStack() {
        findNavController().popBackStack()
    }

    // Abstract methods

    /**
     * Called during the [Fragment.onCreateView] lifecycle method,
     * override to inflate layout w/ ViewBinding
     */
    protected abstract fun inflateLayout(container: ViewGroup?): BINDING

    /**
     * Called during the [Fragment.onViewCreated] lifecycle method,
     * override to initialize views
     */
    protected abstract fun initViews()

    /**
     * Called during the [Fragment.onDestroyView] lifecycle method,
     * override to dispose resources associated to views
     */
    protected abstract fun disposeViews()

    /**
     * Called during the [Fragment.onViewCreated] lifecycle method,
     * override to register view listeners
     */
    protected abstract fun registerListeners()

    /**
     * Called during the [Fragment.onDestroyView] lifecycle method,
     * override to unregister view listeners
     */
    protected abstract fun unregisterListeners()

    /**
     * Called during the [Fragment.onViewCreated] lifecycle method,
     * override to load data from the view model cache or from the network
     */
    protected abstract fun loadData()

    /**
     * Called during the [Fragment.onViewCreated] lifecycle method,
     * override to restore data saved in the instance state bundle
     */
    protected open fun restoreInstanceState(savedInstanceState: Bundle) {
        // empty implementation
    }

    /**
     * Override to handle the event in specific fragments
     */
    open fun handleSuccessfulFirebaseSignIn() {
        // empty implementation
    }

    /**
     * Override to handle the event in specific fragments
     */
    open fun handleFailedFirebaseSignIn(t: Throwable?) {
        // empty implementation
    }

    /**
     * Override to return the presenter
     */
    abstract fun <T : BaseMvp.View> getPresenter(): Presenter<T>

    /**
     * Override to implement fragment-specific behavior on back press
     *
     * @return true if the event was consumed, false otherwise
     */
    open fun onBackPressed() = false

    /**
     * Override to return the preferred container for info messages
     */
    abstract fun getInfoBarContainer(): ViewGroup

    /**
     * Override to inject dependencies
     */
    protected open fun injectDependencies() {
        // empty implementation
    }
}