package com.busytrack.openlivetrivia.screen.moderatereports

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.generic.viewpager.PagerViewListener
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import kotlinx.android.synthetic.main.fragment_moderate_reports.*
import javax.inject.Inject

class ModerateReportsFragment : BaseFragment(),
    ModerateReportsMvp.View,
    PagerViewListener,
    ModerateReportsItemContract {

    @Inject
    lateinit var presenter: ModerateReportsMvp.Presenter

    @Inject
    lateinit var activityContract: ActivityContract

    @Inject
    lateinit var dialogManager: DialogManager

    private var reportedEntriesTab = ModerateReportsReportedEntriesTab(
        this,
        object : ModerateReportsTabContract {
            override fun onRefreshTriggered() {
                presenter.requestReportedEntries(true)
            }

            override fun onScrollThresholdReached() {
                presenter.onReportedEntriesScrollThresholdReached()
            }
        }
    )
    private var bannedEntriesTab = ModerateReportsBannedEntriesTab(
        this,
        object : ModerateReportsTabContract {
            override fun onRefreshTriggered() {
                presenter.requestBannedEntries(true)
            }

            override fun onScrollThresholdReached() {
                presenter.onBannedEntriesScrollThresholdReached()
            }
        }
    )

    // Fragment lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moderate_reports, container, false)
    }

    // Pager View Listener

    override fun onViewInflated(view: View) {
        when(view.id) {
            R.id.swipe_refresh_reported_entries -> reportedEntriesTab.onViewInflated(view)
            R.id.swipe_refresh_banned_entries -> bannedEntriesTab.onViewInflated(view)
        }
    }

    override fun onViewDestroyed(view: View) {
        when(view.id) {
            R.id.swipe_refresh_reported_entries -> reportedEntriesTab.onViewDestroyed(view)
            R.id.swipe_refresh_banned_entries -> bannedEntriesTab.onViewDestroyed(view)
        }
    }

    // List Item Contract

    override fun onBanClicked(model: EntryReportModel) {
        dialogManager.showAlertDialog(
            titleResId = R.string.dialog_title_confirmation,
            messageResId = R.string.dialog_message_confirm_ban,
            positiveButtonClickListener = { _, _ ->
                presenter.banEntry(model)
            }
        )
    }

    override fun onUnbanClicked(model: EntryReportModel) {
        dialogManager.showAlertDialog(
            titleResId = R.string.dialog_title_confirmation,
            messageResId = R.string.dialog_message_confirm_unban,
            positiveButtonClickListener = { _, _ ->
                presenter.unbanEntry(model)
            }
        )
    }

    override fun onDismissClicked(model: EntryReportModel) {
        dialogManager.showAlertDialog(
            titleResId = R.string.dialog_title_confirmation,
            messageResId = R.string.dialog_message_confirm_dismiss_report,
            positiveButtonClickListener = { _, _ ->
                presenter.dismissReport(model)
            }
        )
    }

    // BaseFragment implementation

    override fun initViews() {
        moderate_reports_view_pager.adapter = ModerateReportsPagerAdapter(context!!, this)
        moderate_reports_tab_layout.setupWithViewPager(moderate_reports_view_pager)
    }

    override fun disposeViews() {
        moderate_reports_view_pager.adapter = null
        moderate_reports_tab_layout.setupWithViewPager(null)
    }

    override fun registerListeners() {
    }

    override fun unregisterListeners() {
    }

    override fun loadData() {
        presenter.requestReportedEntries()
        presenter.requestBannedEntries()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = moderate_reports_root_view

    override fun injectDependencies() {
        (this.context as BaseActivity).activityComponent.inject(this)
    }

    // Mvp Implementation

    override fun setReportedEntriesRefreshingIndicator(refreshing: Boolean) {
        reportedEntriesTab.refreshing = refreshing
    }

    override fun setBannedEntriesRefreshingIndicator(refreshing: Boolean) {
        bannedEntriesTab.refreshing = refreshing
    }

    override fun updateReportedEntries(reportedEntries: List<EntryReportModel>) {
        reportedEntriesTab.submitReports(reportedEntries)
    }

    override fun updateBannedEntries(bannedEntries: List<EntryReportModel>) {
        bannedEntriesTab.submitReports(bannedEntries)
    }

    override fun updateReportedEntriesLoadMoreState(loading: Boolean) {
        reportedEntriesTab.loadingMore = loading
    }

    override fun updateBannedEntriesLoadMoreState(loading: Boolean) {
        bannedEntriesTab.loadingMore = loading
    }

    companion object {
        fun newInstance() = ModerateReportsFragment()
    }
}
