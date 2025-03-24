package com.busytrack.openlivetrivia.screen.moderatereports

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.LayoutTabModerateReportsBinding
import com.busytrack.openlivetrivia.extension.requestApplyInsetsWhenAttached
import com.busytrack.openlivetrivia.generic.recyclerview.ListItemDecoration
import com.busytrack.openlivetrivia.generic.viewpager.PagerViewListener
import com.busytrack.openlivetrivia.view.ScrollAwareRecyclerView
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel

class ModerateReportsTab(
    listItemContract: ModerateReportsItemContract,
    val tabContract: ModerateReportsTabContract
) : PagerViewListener<LayoutTabModerateReportsBinding> {

    private var _binding: LayoutTabModerateReportsBinding? = null
    private val binding get() = _binding
    private var adapter = ModerateReportsListAdapter(contract = listItemContract)
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

    override fun onViewInflated(index: Int, binding: LayoutTabModerateReportsBinding) {
        setBinding(binding)
        binding.apply {
            ViewCompat.setOnApplyWindowInsetsListener(recyclerViewModerateEntries) { v, windowInsets ->
                val insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() or
                            WindowInsetsCompat.Type.displayCutout()
                )
                v.updatePadding(
                    bottom = insets.bottom,
                    left = insets.left,
                    right = insets.right
                )
                WindowInsetsCompat.CONSUMED
            }
            recyclerViewModerateEntries.requestApplyInsetsWhenAttached()
            swipeRefreshModerateEntries.setColorSchemeResources(R.color.colorAccent)
            swipeRefreshModerateEntries.setOnRefreshListener {
                this@ModerateReportsTab.clearReports()
                tabContract.onRefreshTriggered()
            }
            setupRecyclerView(recyclerViewModerateEntries)
        }
        syncState()
    }

    override fun onViewDestroyed(index: Int) {
        binding?.apply {
            recyclerViewModerateEntries.adapter = null
            recyclerViewModerateEntries.removeItemDecorationAt(0)
        }
        clearBinding()
    }

    private fun setRefreshIndicatorState(refreshing: Boolean) {
        binding?.swipeRefreshModerateEntries?.isRefreshing = refreshing
    }

    private fun syncState() {
        binding?.swipeRefreshModerateEntries?.isRefreshing = refreshing
    }

    private fun setLoadMoreIndicatorState(loading: Boolean) {
        adapter.apply { if (loading) showLoadingPlaceholder() else hideLoadingPlaceholder() }
    }

    protected fun setBinding(binding: LayoutTabModerateReportsBinding) {
        _binding = binding
    }

    protected fun clearBinding() {
        _binding = null
    }

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