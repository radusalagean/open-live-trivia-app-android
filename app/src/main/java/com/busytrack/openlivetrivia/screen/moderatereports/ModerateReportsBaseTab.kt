package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.View
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.recyclerview.ListItemDecoration
import com.busytrack.openlivetrivia.generic.viewpager.PagerViewListener
import com.busytrack.openlivetrivia.view.ScrollAwareRecyclerView
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel

abstract class ModerateReportsBaseTab( // TODO test
    listItemContract: ModerateReportsItemContract,
    protected val tabContract: ModerateReportsTabContract
) : PagerViewListener {

    protected var rootView: View? = null
    protected var adapter = ModerateReportsListAdapter(contract = listItemContract)
    var refreshing: Boolean = false
        set(value) {
            field = value
            setRefreshIndicatorState(value)
        }

    var loadingMore: Boolean = false
        set(value) {
            field = value
            setLoadMoreIndicatorState(value)
        }

    protected abstract fun setRefreshIndicatorState(refreshing: Boolean)

    private fun setLoadMoreIndicatorState(loading: Boolean) {
        adapter.apply { if (loading) showLoadingPlaceholder() else hideLoadingPlaceholder() }
    }

    abstract fun syncState()

    fun submitReports(reports: List<EntryReportModel>) {
        adapter.setList(reports)
    }

    fun clearReports() {
        adapter.clearList()
    }

    protected fun setupRecyclerView(recyclerView: ScrollAwareRecyclerView) {
        recyclerView.adapter = adapter
        recyclerView.listener = object : ScrollAwareRecyclerView.Listener {
            override fun onThresholdExceeded() {
                tabContract.onScrollThresholdReached()
            }
        }
        with (recyclerView.context.resources) {
            recyclerView.addItemDecoration(ListItemDecoration(
                0,
                getDimensionPixelSize(R.dimen.screen_vertical_padding),
                0,
                getDimensionPixelSize(R.dimen.screen_vertical_padding)
            ))
        }
    }
}