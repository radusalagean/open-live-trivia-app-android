package com.busytrack.openlivetrivia.screen.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.busytrack.openlivetrivia.BuildConfig

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.extension.setVisibleSoft
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import kotlinx.android.synthetic.main.fragment_authentication.*
import kotlinx.android.synthetic.main.layout_register.*
import kotlinx.android.synthetic.main.layout_register.view.*
import javax.inject.Inject

class AuthenticationFragment : BaseFragment(), AuthenticationMvp.View, AuthenticationContract {
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

    override fun onAttach(context: Context) {
        (this.context as BaseActivity).activityComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.checkServerCompatibility()
    }

    // BaseFragment implementation

    override fun setRefreshingIndicator(refreshing: Boolean) {
        progress_bar_main.setVisibleSoft(refreshing)
        view_pager.setVisibleSoft(!refreshing)
    }

    override fun initViews() {
        view_pager.isUserInputEnabled = false
        view_pager.adapter = pagerAdapter
    }

    override fun disposeViews() {
        view_pager.adapter = null
    }

    override fun registerListeners() {

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

    override fun getInfoBarContainer(): ViewGroup = authentication_root_view

    // Mvp Implementation

    override fun showRegisterPage() {
        view_pager.currentItem = AuthenticationPageType.REGISTER.ordinal
    }

    override fun setUsernameAvailability(available: Boolean) {
        pagerAdapter.registerViewHolder?.itemView?.apply {
            text_view_username_availability?.apply {
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
            button_register.isEnabled = available
        }
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
        dialogManager.showAlertDialog(
            R.string.dialog_title_register_disclaimer,
            R.string.dialog_message_register_disclaimer,
            R.string.dialog_positive_register_disclaimer,
            R.string.dialog_negative_register_disclaimer
        ) { _, _ ->
            presenter.register(username)
        }
    }

    override fun onUsernameChanged(username: String) {
        clearUsernameAvailability()
        presenter.dispose()
        if (username.isNotBlank() && username.length >= 4) {
            presenter.checkUsernameAvailability(username.trim())
        }
    }

    override fun onChangeAccountPressed() {
        edit_text_username.text = null
        view_pager.currentItem = AuthenticationPageType.LOG_IN.ordinal
        presenter.firebaseLogOut()
    }

    // Private implementation

    private fun clearUsernameAvailability() {
        text_view_username_availability.setVisibleSoft(false)
        text_view_username_availability.text = null
        button_register.isEnabled = false
    }

    companion object {
        fun newInstance() = AuthenticationFragment()
    }
}
