package com.busytrack.openlivetrivia.screen.settings

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.preference.Preference
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.generic.settings.PreferenceFragmentCompat
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var presenter: SettingsMvp.Presenter

    @Inject
    lateinit var dialogManager: DialogManager

    override fun onAttach(context: Context) {
        (this.context as BaseActivity).activityComponent.inject(this)
        super.onAttach(context)
    }

    // BaseFragment implementation

    override fun initViews() {

    }

    override fun disposeViews() {
    }

    override fun registerListeners() {
    }

    override fun unregisterListeners() {
    }

    override fun loadData() {
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup =
        view!! as ViewGroup

    // PreferenceFragmentCompat implementation

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference?.key == getString(R.string.pref_key_delete_account)) {
            dialogManager.showAlertDialog(
                titleResId = R.string.dialog_delete_account_title,
                messageResId = R.string.dialog_delete_account_message,
                positiveButtonClickListener = { _, _ ->
                    presenter.deleteAccount()
                }
            )
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}