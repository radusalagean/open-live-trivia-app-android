package com.busytrack.openlivetrivia.screen.mainmenu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.view.COIN_ACCELERATE_LONG
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.layout_header_user.*
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
        authenticationManager.getAuthenticatedUser()?.let {
            // Since we already have user info stored, display it and
            // also request the latest user info in the loadData() method
            // Thus, there won't be any need for progress bars (silent operation)
            updateAccountInfo(it)
        }
    }

    override fun disposeViews() {
        context?.let { Glide.with(it.applicationContext).clear(image_view_my_profile) }
    }

    override fun registerListeners() {
        button_play.setOnClickListener {
            activityContract.showGameScreen()
        }
        button_leaderboard.setOnClickListener {
            activityContract.showLeaderboardScreen()
        }
        button_log_out.setOnClickListener {
            authenticationManager.signOut()
        }
    }

    override fun unregisterListeners() {
        button_play.setOnClickListener(null)
        button_leaderboard.setOnClickListener(null)
        button_log_out.setOnClickListener(null)
    }

    override fun loadData() {
        presenter.requestMyAccountInfo()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = main_menu_root_layout

    // Mvp Contract

    override fun updateAccountInfo(userModel: UserModel) {
        with(userModel) {
            text_view_my_username.text = username
            Glide.with(image_view_my_profile)
                .load(UserModel.getThumbnailPath(userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(image_view_my_profile)
            text_view_coins.apply {
                if (coins == null) {
                    setCoins(userModel.coins!!)
                } else {
                    // If we have a previous value for "coins",
                    // animate the transition to the new value
                    updateValue(userModel.coins!!, COIN_ACCELERATE_LONG)
                }
            }
        }
    }

    companion object {
        fun newInstance() = MainMenuFragment()
    }
}
