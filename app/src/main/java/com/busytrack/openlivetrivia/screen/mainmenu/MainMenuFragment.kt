package com.busytrack.openlivetrivia.screen.mainmenu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.BuildConfig

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.busytrack.openlivetrivia.sound.SoundManager
import com.busytrack.openlivetrivia.view.COIN_ACCELERATE_LONG
import com.busytrack.openlivetrivia.view.COIN_DISPLAY_FORMAT
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.layout_header_user.*
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.sign

class MainMenuFragment : BaseFragment(), MainMenuMvp.View {
    @Inject
    lateinit var presenter: MainMenuMvp.Presenter

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Inject
    lateinit var activityContract: ActivityContract

    @Inject
    lateinit var soundManager: SoundManager

    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    @Inject
    lateinit var dialogManager: DialogManager

    // Lifecycle callbacks

    override fun onAttach(context: Context) {
        (this.context as BaseActivity).activityComponent.inject(this)
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
        image_view_project_link.setOnClickListener {
            activityContract.openLinkInBrowser(BuildConfig.APP_PROJECT_LINK)
        }
        button_play.setOnClickListener {
            joinGame()
        }
        button_leaderboard.setOnClickListener {
            activityContract.showLeaderboardScreen()
        }
        button_moderate_reports.setOnClickListener {
            activityContract.showModerateReportsScreen()
        }
        button_settings.setOnClickListener {
            activityContract.showSettingsFragment()
        }
        button_log_out.setOnClickListener {
            authenticationManager.signOut()
        }
    }

    override fun unregisterListeners() {
        image_view_project_link.setOnLongClickListener(null)
        button_play.setOnClickListener(null)
        button_leaderboard.setOnClickListener(null)
        button_moderate_reports.setOnClickListener(null)
        button_settings.setOnClickListener(null)
        button_log_out.setOnClickListener(null)
    }

    override fun loadData() {
        presenter.requestMyAccountInfo()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = main_menu_root_layout

    // Mvp Implementation

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
                    // and play the appropriate sound effect
                    val diff = userModel.coins!! - coins!!
                    val diffString = COIN_DISPLAY_FORMAT.format(diff.absoluteValue)
                    if (diff.sign > 0.0) {
                        soundManager.won()
                        activityContract.showInfoMessage(
                            R.string.message_amount_won,
                            diffString
                        )
                    } else if (diff.sign < 0.0f) {
                        soundManager.lost()
                        activityContract.showWarningMessage(
                            R.string.message_amount_lost,
                            diffString
                        )
                    }
                    updateValue(userModel.coins!!, COIN_ACCELERATE_LONG)
                }
            }
            button_moderate_reports.visibility =
                if (userModel.rights!!.ordinal >= UserRightsLevel.MOD.ordinal) { // MOD or higher rights level
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    // Private

    private fun joinGame() {
        if (sharedPreferencesRepository.isShowRulesOnGameJoinEnabled()) {
            dialogManager.showAlertDialog(
                R.string.game_dialog_title_rules,
                R.string.game_dialog_message_rules,
                R.string.game_dialog_positive_button_rules,
                R.string.game_dialog_negative_button_rules
            ) { _, _ ->
                activityContract.showGameScreen()
            }
        } else {
            activityContract.showGameScreen()
        }
    }

    companion object {
        fun newInstance() = MainMenuFragment()
    }
}
