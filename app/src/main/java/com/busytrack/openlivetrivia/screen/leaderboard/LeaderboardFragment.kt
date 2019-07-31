package com.busytrack.openlivetrivia.screen.leaderboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.generic.recyclerview.ListItemDecoration
import com.busytrack.openlivetrivia.rights.RightsManager
import com.busytrack.openlivetrivia.view.ScrollAwareRecyclerView
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import timber.log.Timber
import javax.inject.Inject

class LeaderboardFragment : BaseFragment(), LeaderboardMvp.View, LeaderboardItemContract,
    ScrollAwareRecyclerView.Listener {

    @Inject
    lateinit var presenter: LeaderboardMvp.Presenter

    @Inject
    lateinit var rightsManager: RightsManager

    private var leaderboardAdapter: LeaderboardAdapter? = null

    // Lifecycle callbacks

    override fun onAttach(context: Context) {
        (this.context as BaseActivity).activityComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

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

    override fun initViews() {
        with(leaderboard_recycler_view) {
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
        leaderboard_swipe_refresh_layout.setColorSchemeResources(R.color.colorAccent)
    }

    override fun disposeViews() {
        with(leaderboard_recycler_view) {
            layoutManager = null
            adapter = null
            removeItemDecorationAt(0)
        }
        leaderboardAdapter = null
    }

    override fun registerListeners() {
        leaderboard_swipe_refresh_layout.setOnRefreshListener {
            triggerFullRefresh()
        }
        leaderboard_recycler_view.listener = this
    }

    override fun unregisterListeners() {
        leaderboard_swipe_refresh_layout.setOnRefreshListener(null)
        leaderboard_recycler_view.listener = null
    }

    override fun loadData() {
        presenter.requestLeaderboard()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun getInfoBarContainer(): ViewGroup = leaderboard_swipe_refresh_layout

    override fun setRefreshingIndicator(refreshing: Boolean) {
        leaderboard_swipe_refresh_layout.isRefreshing = refreshing
    }

    private fun triggerFullRefresh() {
        leaderboardAdapter?.clearList()
        presenter.requestLeaderboard(invalidate = true)
    }

    companion object {
        fun newInstance() = LeaderboardFragment()
    }
}
