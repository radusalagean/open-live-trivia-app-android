package com.busytrack.openlivetrivia.screen.authentication

import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.databinding.FragmentAuthenticationBinding
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.extension.setVisibleSoft
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import javax.inject.Inject

open class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>(),
    AuthenticationMvp.View, AuthenticationContract {
    @Inject
    lateinit var presenter: AuthenticationMvp.Presenter

    @Inject
    lateinit var authorizationManager: AuthorizationManager

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var activityContract: ActivityContract

    private val pagerAdapter = AuthenticationAdapter(this)

    // Lifecycle callbacks

    override fun onStart() {
        super.onStart()
        presenter.checkServerCompatibility()
    }

    // BaseFragment implementation

    override fun inflateLayout(container: ViewGroup?): FragmentAuthenticationBinding {
        return FragmentAuthenticationBinding.inflate(
            layoutInflater, container, false
        )
    }

    override fun setRefreshingIndicator(refreshing: Boolean) {
        binding.progressBarMain.setVisibleSoft(refreshing)
        binding.viewPager.setVisibleSoft(!refreshing)
    }

    override fun initViews() {
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = pagerAdapter
    }

    override fun disposeViews() {
        binding.viewPager.adapter = null
    }

    override fun registerListeners() {
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
    }

    override fun unregisterListeners() {

    }

    override fun loadData() {
    }

    override fun handleSuccessfulFirebaseSignIn() {
        super.handleSuccessfulFirebaseSignIn()
        // We can now log in with the backend
        presenter.login()
    }

    override fun handleFailedFirebaseSignIn(t: Throwable?) {
        super.handleFailedFirebaseSignIn(t)
        presenter.refreshing = false
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = binding.authenticationRootView

    override fun injectDependencies() {
        (this.context as BaseActivity).activityComponent.inject(this)
    }

    // Mvp Implementation

    override fun showRegisterPage() {
        binding.viewPager.currentItem = AuthenticationPageType.REGISTER.ordinal
    }

    override fun setUsernameAvailability(available: Boolean) {
        pagerAdapter.registerViewHolder?.binding?.apply {
            textViewUsernameAvailability.apply {
                setText(if (available) R.string.username_available else R.string.username_unavailable)
                setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        if (available) R.color.colorPositive else R.color.colorNegative,
                        null
                    )
                )
                setVisibleSoft(true)
            }
            buttonRegister.isEnabled = available
        }
    }

    override fun showMainMenuScreen() {
        navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToMainMenuFragment())
    }

    // Authentication Contract

    override fun onLoginPressed() {
        if (authorizationManager.isUserAuthenticated()) {
            // if a Google account is already selected and authorized
            // Log in with the backend
            presenter.login()
        } else {
            // Open the account selection dialog
            presenter.firebaseLogIn()
        }
    }

    override fun onPrivacyPolicyLinkPressed() {
        activityContract.openLinkInBrowser(BuildConfig.APP_PRIVACY_POLICY_LINK)
    }

    override fun onRegisterPressed(username: String) {
        presenter.register(username)
    }

    override fun onUsernameChanged(username: String) {
        clearUsernameAvailability()
        presenter.dispose()
        if (username.isNotBlank() && username.length >= 4) {
            presenter.checkUsernameAvailability(username.trim())
        }
    }

    override fun onChangeAccountPressed() {
        pagerAdapter.registerViewHolder?.binding?.editTextUsername?.text = null
        binding.viewPager.currentItem = AuthenticationPageType.LOG_IN.ordinal
        presenter.firebaseLogOut()
    }

    // Private implementation

    private fun clearUsernameAvailability() {
        pagerAdapter.registerViewHolder?.binding?.apply {
            textViewUsernameAvailability.apply {
                setVisibleSoft(false)
                text = null
            }
            buttonRegister.isEnabled = false
        }
    }
}
