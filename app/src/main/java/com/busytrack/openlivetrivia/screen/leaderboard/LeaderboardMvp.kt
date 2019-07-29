package com.busytrack.openlivetrivia.screen.leaderboard

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.Observable

interface LeaderboardMvp {

    interface Model : BaseMvp.Model<LeaderboardViewModel> {
        fun initLeaderboard(): Observable<PaginatedResponseModel<UserModel>>
        fun getNextLeaderboardPage(): Observable<PaginatedResponseModel<UserModel>>
        fun getCachedLeaderboard(): Observable<List<UserModel>>
        fun upgradeToMod(userId: String): Observable<MessageModel>
        fun downgradeToRegular(userId: String): Observable<MessageModel>
    }

    interface View : BaseMvp.View {
        fun updateLeaderboard(users: List<UserModel>)
        fun updateLoadMoreState(loading: Boolean)
        fun onUserRightsChanged()
    }

    interface Presenter : BaseMvp.Presenter<View> {
        fun requestLeaderboard(invalidate: Boolean = false)
        fun onScrollThresholdReached()
        fun upgradeToMod(user: UserModel)
        fun downgradeToRegular(user: UserModel)
    }
}