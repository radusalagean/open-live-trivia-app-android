package com.busytrack.openlivetrivia.screen.mainmenu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import kotlinx.android.synthetic.main.fragment_main_menu.*
import javax.inject.Inject

class MainMenuFragment : BaseFragment(), MainMenuMvp.View {
    @Inject
    lateinit var presenter: MainMenuMvp.Presenter

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Inject
    lateinit var activityContract: ActivityContract

    // Lifecycle callbacks

    override fun onAttach(context: Context) {
        activityComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    // BaseFragment implementation

    override fun initViews() {

    }

    override fun disposeViews() {

    }

    override fun registerListeners() {
        button_play.setOnClickListener {
            activityContract.showGameScreen()
        }
        button_log_out.setOnClickListener {
            authenticationManager.signOut()
        }
    }

    override fun unregisterListeners() {
        button_play.setOnClickListener(null)
        button_log_out.setOnClickListener(null)
    }

    override fun loadData() {
        authenticationManager.getAuthenticatedUser()?.let {
            text_view_coins.setCoins(it.coins!!)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = main_menu_root_layout

    // Mvp Contract

    // TODO

    companion object {
        fun newInstance() = MainMenuFragment()
    }
}
