package com.busytrack.openlivetrivia.screen.moderatereports

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.LayoutTabModerateReportsBinding

class ModerateReportsBannedEntriesTab(
    listItemContract: ModerateReportsItemContract,
    tabContract: ModerateReportsTabContract
) : ModerateReportsBaseTab(listItemContract, tabContract) {

    override fun onViewInflated(index: Int, binding : LayoutTabModerateReportsBinding) {
        setBinding(binding)
        binding.apply {
            swipeRefreshModerateEntries.setColorSchemeResources(R.color.colorAccent)
            swipeRefreshModerateEntries.setOnRefreshListener {
                this@ModerateReportsBannedEntriesTab.clearReports()
                tabContract.onRefreshTriggered()
            }
            setupRecyclerView(recyclerViewModerateEntries)
        }
        syncState()
    }

    override fun onViewDestroyed(index: Int) {
        binding?.recyclerViewModerateEntries?.adapter = null
        clearBinding()
    }

    override fun setRefreshIndicatorState(refreshing: Boolean) {
        binding?.swipeRefreshModerateEntries?.isRefreshing = refreshing
    }

    override fun syncState() {
        binding?.swipeRefreshModerateEntries?.isRefreshing = refreshing
    }
}