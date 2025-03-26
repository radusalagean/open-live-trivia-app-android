package com.busytrack.openlivetrivia.screen.mainmenu

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.databinding.FragmentMainMenuBinding
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
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.sign

class MainMenuFragment : BaseFragment<FragmentMainMenuBinding>(), MainMenuMvp.View {
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

    // BaseFragment implementation

    override fun inflateLayout(container: ViewGroup?): FragmentMainMenuBinding {
        return FragmentMainMenuBinding.inflate(
            layoutInflater, container, false
        )
    }

    override fun initViews() {
        authenticationManager.getAuthenticatedUser()?.let {
            // Since we already have user info stored, display it and
            // also request the latest user info in the loadData() method
            // Thus, there won't be any need for progress bars (silent operation)
            updateAccountInfo(it)
        }
        binding.textViewVersion.text = "v${BuildConfig.VERSION_NAME}"
    }

    override fun disposeViews() {
        context?.let {
            Glide.with(it.applicationContext).clear(binding.layoutHeaderUser.imageViewMyProfile)
        }
    }

    override fun registerListeners() {
        binding.apply {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
                val insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() or
                            WindowInsetsCompat.Type.displayCutout()
                )
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = insets.left
                    bottomMargin = insets.bottom
                    rightMargin = insets.right
                    topMargin = insets.top
                }
                WindowInsetsCompat.CONSUMED
            }
            imageViewProjectLink.setOnClickListener {
                activityContract.openLinkInBrowser(BuildConfig.APP_PROJECT_LINK)
            }
            buttonPlay.setOnClickListener {
                joinGame()
            }
            buttonLeaderboard.setOnClickListener {
                showLeaderboardScreen()
            }
            buttonModerateReports.setOnClickListener {
                showModerateReportsScreen()
            }
            buttonSettings.setOnClickListener {
                showSettingsScreen()
            }
            buttonLogOut.setOnClickListener {
                authenticationManager.signOut(activityContract = activityContract)
                showAuthenticationScreen()
            }
            privacyPolicyLink.setOnClickListener {
                activityContract.openLinkInBrowser(BuildConfig.APP_PRIVACY_POLICY_LINK)
            }
        }
    }

    override fun unregisterListeners() {
        binding.apply {
            imageViewProjectLink.setOnLongClickListener(null)
            buttonPlay.setOnClickListener(null)
            buttonLeaderboard.setOnClickListener(null)
            buttonModerateReports.setOnClickListener(null)
            buttonSettings.setOnClickListener(null)
            buttonLogOut.setOnClickListener(null)
            privacyPolicyLink.setOnClickListener(null)
        }
    }

    override fun loadData() {
        presenter.requestMyAccountInfo()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = binding.mainMenuRootLayout

    override fun injectDependencies() {
        (this.context as BaseActivity).activityComponent.inject(this)
    }

    // Mvp Implementation

    override fun updateAccountInfo(userModel: UserModel) {
        with(userModel) {
            binding.layoutHeaderUser.apply {
                textViewMyUsername.text = username
                Glide.with(imageViewMyProfile)
                    .load(UserModel.getThumbnailPath(userId))
                    .placeholder(R.drawable.ic_account_circle_accent_24dp)
                    .circleCrop()
                    .into(imageViewMyProfile)
            }

            binding.textViewCoins.apply {
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
            binding.buttonModerateReports.visibility =
                if (userModel.rights!!.ordinal >= UserRightsLevel.MOD.ordinal) { // MOD or higher rights level
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    override fun showGameScreen() {
        navigate(MainMenuFragmentDirections.actionMainMenuFragmentToGameFragment())
    }

    override fun showLeaderboardScreen() {
        navigate(MainMenuFragmentDirections.actionMainMenuFragmentToLeaderboardFragment())
    }

    override fun showModerateReportsScreen() {
        navigate(MainMenuFragmentDirections.actionMainMenuFragmentToModerateReportsFragment())
    }

    override fun showSettingsScreen() {
        navigate(MainMenuFragmentDirections.actionMainMenuFragmentToSettingsFragment())
    }

    override fun showAuthenticationScreen() {
        navigate(MainMenuFragmentDirections.actionMainMenuFragmentToAuthenticationFragment())
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
                showGameScreen()
            }
        } else {
            showGameScreen()
        }
    }
}
