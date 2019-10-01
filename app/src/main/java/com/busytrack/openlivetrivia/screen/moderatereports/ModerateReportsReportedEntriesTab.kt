package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.View
import com.busytrack.openlivetrivia.R
import kotlinx.android.synthetic.main.layout_tab_reported_entries.view.*

class ModerateReportsReportedEntriesTab(
    listItemContract: ModerateReportsItemContract,
    tabContract: ModerateReportsTabContract
) : ModerateReportsBaseTab(listItemContract, tabContract) {

    override fun onViewInflated(view: View) {
        rootView = view
        rootView?.apply {
            swipe_refresh_reported_entries.setColorSchemeResources(R.color.colorAccent)
            swipe_refresh_reported_entries.setOnRefreshListener {
                this@ModerateReportsReportedEntriesTab.clearReports()
                tabContract.onRefreshTriggered()
            }
            setupRecyclerView(recycler_view_reported_entries)
        }
        syncState()
    }

    override fun onViewDestroyed(view: View) {
        rootView?.apply {
            recycler_view_reported_entries.adapter = null
            recycler_view_reported_entries.removeItemDecorationAt(0)
        }
        rootView = null
    }

    override fun setRefreshIndicatorState(refreshing: Boolean) { // TODO test
        rootView?.swipe_refresh_reported_entries?.isRefreshing = refreshing
    }

    override fun syncState() { // TODO test
        rootView?.apply {
            swipe_refresh_reported_entries?.isRefreshing = refreshing
        }
    }
}