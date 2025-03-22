package com.busytrack.openlivetrivia.screen.leaderboard

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.FragmentLeaderboardBinding
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.generic.recyclerview.ListItemDecoration
import com.busytrack.openlivetrivia.rights.RightsManager
import com.busytrack.openlivetrivia.view.ScrollAwareRecyclerView
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import javax.inject.Inject

class LeaderboardFragment : BaseFragment<FragmentLeaderboardBinding>(), LeaderboardMvp.View, LeaderboardItemContract,
    ScrollAwareRecyclerView.Listener {

    @Inject
    lateinit var presenter: LeaderboardMvp.Presenter

    @Inject
    lateinit var rightsManager: RightsManager

    private var leaderboardAdapter: LeaderboardAdapter? = null

    // Lifecycle callbacks

    // Leaderboard Item Contract

    override fun onUserLongClicked(user: UserModel) {
        rightsManager.triggerRightsChange(
            user,
            presenter::upgradeToMod,
            presenter::downgradeToRegular
        )
    }

    // Scroll Aware Recycler view

    override fun onThresholdExceeded() {
        presenter.onScrollThresholdReached()
    }

    // Mvp Implementation

    override fun updateLeaderboard(users: List<UserModel>) {
        leaderboardAdapter?.setList(users)
    }

    override fun updateLoadMoreState(loading: Boolean) {
        if (loading) leaderboardAdapter?.showLoadingPlaceholder() else leaderboardAdapter?.hideLoadingPlaceholder()
    }

    override fun onUserRightsChanged() {
        triggerFullRefresh()
    }

    // BaseFragment implementation

    override fun inflateLayout(container: ViewGroup?): FragmentLeaderboardBinding {
        return FragmentLeaderboardBinding.inflate(layoutInflater, container, false)
    }

    override fun initViews() {
        with(binding.leaderboardRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = LeaderboardAdapter(this@LeaderboardFragment, arrayListOf()).also {
                leaderboardAdapter = it
            }
            addItemDecoration(ListItemDecoration(
                0,
                context.resources.getDimensionPixelSize(R.dimen.screen_vertical_padding),
                0,
                context.resources.getDimensionPixelSize(R.dimen.screen_vertical_padding)
            ))
        }
        binding.leaderboardSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
    }

    override fun disposeViews() {
        with(binding.leaderboardRecyclerView) {
            layoutManager = null
            adapter = null
            removeItemDecorationAt(0)
        }
        leaderboardAdapter = null
    }

    override fun registerListeners() {
        binding.leaderboardSwipeRefreshLayout.setOnRefreshListener {
            triggerFullRefresh()
        }
        binding.leaderboardRecyclerView.listener = this
    }

    override fun unregisterListeners() {
        binding.leaderboardSwipeRefreshLayout.setOnRefreshListener(null)
        binding.leaderboardRecyclerView.listener = null
    }

    override fun loadData() {
        presenter.requestLeaderboard()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = binding.leaderboardSwipeRefreshLayout

    override fun setRefreshingIndicator(refreshing: Boolean) {
        binding.leaderboardSwipeRefreshLayout.isRefreshing = refreshing
    }

    override fun injectDependencies() {
        (this.context as BaseActivity).activityComponent.inject(this)
    }

    // Private

    private fun triggerFullRefresh() {
        leaderboardAdapter?.clearList()
        presenter.requestLeaderboard(invalidate = true)
    }
}
