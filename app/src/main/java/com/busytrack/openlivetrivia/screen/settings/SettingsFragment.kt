package com.busytrack.openlivetrivia.screen.settings

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import androidx.viewbinding.ViewBinding
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.databinding.FragmentBaseTestBinding
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.generic.settings.PreferenceFragmentCompat
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), SettingsMvp.View {

    @Inject
    lateinit var presenter: SettingsMvp.Presenter

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var authenticationManager: AuthenticationManager


    // BaseFragment implementation

    override fun inflateLayout(container: ViewGroup?): ViewBinding {
        return FragmentBaseTestBinding.inflate(layoutInflater, container, false)
    }

    override fun initViews() {
        findPreference<PreferenceGroup>(getString(R.string.pref_category_key_danger_zone))?.let { prefGroup ->
            prefGroup.findPreference<Preference>(
                getString(R.string.pref_key_disconnect_everyone)
            )?.let { pref ->
                authenticationManager.getAuthenticatedUser()?.takeUnless { user ->
                    user.rights == UserRightsLevel.ADMIN
                }?.let {
                    prefGroup.removePreference(pref)
                }
            }
        }
    }

    override fun disposeViews() {
    }

    override fun registerListeners() {
        ViewCompat.setOnApplyWindowInsetsListener(requireView()) { v, windowInsets ->
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

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup =
        requireView() as ViewGroup

    override fun injectDependencies() {
        (this.context as BaseActivity).activityComponent.inject(this)
    }

    // Mvp Implementation

    override fun showAuthenticationScreen() {
        navigate(SettingsFragmentDirections.actionSettingsFragmentToAuthenticationFragment())
    }

    // PreferenceFragmentCompat implementation

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            getString(R.string.pref_key_delete_account) -> {
                dialogManager.showAlertDialog(
                    titleResId = R.string.dialog_delete_account_title,
                    messageResId = R.string.dialog_delete_account_message,
                    positiveButtonClickListener = { _, _ ->
                        presenter.deleteAccount()
                    }
                )
                return true
            }
            getString(R.string.pref_key_disconnect_everyone) -> {
                dialogManager.showAlertDialog(
                    titleResId = R.string.dialog_disconnect_everyone_title,
                    messageResId = R.string.dialog_disconnect_everyone_message,
                    positiveButtonClickListener = { _, _ ->
                        presenter.disconnectEveryone()
                    }
                )
                return true
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}