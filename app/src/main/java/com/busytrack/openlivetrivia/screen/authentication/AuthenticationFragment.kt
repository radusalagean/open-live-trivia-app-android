package com.busytrack.openlivetrivia.screen.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import kotlinx.android.synthetic.main.fragment_authentication.*
import javax.inject.Inject


class AuthenticationFragment : BaseFragment(), AuthenticationMvp.View {
    @Inject
    lateinit var presenter: AuthenticationMvp.Presenter

    override fun onAttach(context: Context) {
        activityComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun initViews() {

    }

    override fun disposeViews() {

    }

    override fun registerListeners() {
        button_sign_in.setOnClickListener {
            presenter.signIn()
        }
        button_sign_in.setOnLongClickListener {
            presenter.signOut()
            true
        }
    }

    override fun unregisterListeners() {
        button_sign_in.setOnClickListener(null)
        button_sign_in.setOnLongClickListener(null)
    }

    override fun loadData() {
    }

    override fun handleSuccessfulFirebaseSignIn() {
        super.handleSuccessfulFirebaseSignIn()
        presenter.refreshing = false
    }

    override fun handleFailedFirebaseSignIn(t: Throwable?) {
        super.handleFailedFirebaseSignIn(t)
        presenter.refreshing = false
    }

    override fun getProgressBar() = progress_bar_main

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    companion object {
        @JvmStatic
        fun newInstance() = AuthenticationFragment()
    }
}
