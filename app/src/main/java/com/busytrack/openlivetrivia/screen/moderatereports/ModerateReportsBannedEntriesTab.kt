package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.View
import com.busytrack.openlivetrivia.R
import kotlinx.android.synthetic.main.layout_tab_banned_entries.view.*

class ModerateReportsBannedEntriesTab(
    listItemContract: ModerateReportsItemContract,
    tabContract: ModerateReportsTabContract
) : ModerateReportsBaseTab(listItemContract, tabContract) {

    override fun onViewInflated(view: View) {
        rootView = view
        rootView?.apply {
            swipe_refresh_banned_entries.setColorSchemeResources(R.color.colorAccent)
            swipe_refresh_banned_entries.setOnRefreshListener {
                this@ModerateReportsBannedEntriesTab.clearReports()
                tabContract.onRefreshTriggered()
            }
            setupRecyclerView(recycler_view_banned_entries)
        }
        syncState()
    }

    override fun onViewDestroyed(view: View) {
        rootView?.apply {
            recycler_view_banned_entries.adapter = null
        }
        rootView = null
    }

    override fun setRefreshIndicatorState(refreshing: Boolean) { // TODO test
        rootView?.swipe_refresh_banned_entries?.isRefreshing = refreshing
    }

    override fun syncState() { // TODO test
        rootView?.apply {
            swipe_refresh_banned_entries?.isRefreshing = refreshing
        }
    }
}