package com.busytrack.openlivetrivia.screen.leaderboard

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetrivia.persistence.database.DatabaseRepository
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import io.reactivex.Observable

class LeaderboardModel(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) : BaseModel<LeaderboardViewModel>(), LeaderboardMvp.Model {

    override fun initLeaderboard(): Observable<PaginatedResponseModel<UserModel>> =
        networkRepository.getLeaderboard()
            .doOnNext {
                // Successful response, invalidate cached users and insert fresh data
                databaseRepository.clearUsers()
                viewModel.users.clear()
                handleReceivedResponse(it)
            }

    override fun getNextLeaderboardPage(): Observable<PaginatedResponseModel<UserModel>> =
        networkRepository.getLeaderboard(viewModel.nextAvailablePage!!).
            doOnNext {
                handleReceivedResponse(it)
            }

    override fun getCachedLeaderboard(): Observable<List<UserModel>> =
        databaseRepository.getAllUsers()
            .doOnNext {
                viewModel.users.clear()
                viewModel.users.addAll(it)
            }

    override fun upgradeToMod(userId: String) =
        networkRepository.updateUserRights(userId, UserRightsLevel.MOD.ordinal)

    override fun downgradeToRegular(userId: String) =
        networkRepository.updateUserRights(userId, UserRightsLevel.REGULAR.ordinal)

    private fun handleReceivedResponse(response: PaginatedResponseModel<UserModel>) {
        databaseRepository.insertUsers(response.items)
        viewModel.users.addAll(response.items)
        viewModel.nextAvailablePage = with(response) {
            if (page < pages) page + 1 else null
        }
    }
}