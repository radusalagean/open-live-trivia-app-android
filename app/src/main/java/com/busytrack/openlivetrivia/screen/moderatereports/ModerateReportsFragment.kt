package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.ViewGroup
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.FragmentModerateReportsBinding
import com.busytrack.openlivetrivia.databinding.LayoutTabModerateReportsBinding
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.generic.viewpager.PagerViewListener
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import javax.inject.Inject

class ModerateReportsFragment : BaseFragment<FragmentModerateReportsBinding>(),
    ModerateReportsMvp.View,
    PagerViewListener<LayoutTabModerateReportsBinding>,
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

    // Pager View Listener

    override fun onViewInflated(index: Int, binding: LayoutTabModerateReportsBinding) {
        when(index) {
            ModerateReportsPagerAdapter.INDEX_TAB_REPORTED_ENTRIES ->
                reportedEntriesTab.onViewInflated(index, binding)
            ModerateReportsPagerAdapter.INDEX_TAB_BANNED_ENTRIES ->
                bannedEntriesTab.onViewInflated(index, binding)
        }
    }

    override fun onViewDestroyed(index: Int) {
        when(index) {
            ModerateReportsPagerAdapter.INDEX_TAB_REPORTED_ENTRIES ->
                reportedEntriesTab.onViewDestroyed(index)
            ModerateReportsPagerAdapter.INDEX_TAB_BANNED_ENTRIES ->
                bannedEntriesTab.onViewDestroyed(index)
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

    override fun inflateLayout(container: ViewGroup?): FragmentModerateReportsBinding {
        return FragmentModerateReportsBinding.inflate(
            layoutInflater, container, false
        )
    }

    override fun initViews() {
        binding.moderateReportsViewPager.adapter = ModerateReportsPagerAdapter(requireContext(), this)
        binding.moderateReportsTabLayout.setupWithViewPager(binding.moderateReportsViewPager)
    }

    override fun disposeViews() {
        binding.moderateReportsViewPager.adapter = null
        binding.moderateReportsTabLayout.setupWithViewPager(null)
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

    override fun getInfoBarContainer(): ViewGroup = binding.moderateReportsRootView

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
}
