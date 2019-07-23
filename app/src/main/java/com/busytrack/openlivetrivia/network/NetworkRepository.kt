package com.busytrack.openlivetrivia.network

import com.busytrack.openlivetriviainterface.rest.OpenLiveTriviaApiService
import com.busytrack.openlivetriviainterface.rest.model.*
import com.busytrack.openlivetriviainterface.socket.model.OutgoingAuthenticationModel
import io.reactivex.Completable
import io.reactivex.Observable

class NetworkRepository(private val openLiveTriviaApiService: OpenLiveTriviaApiService) {

    // Users

    fun registerUser(registerModel: OutgoingRegisterModel): Observable<UserModel> =
        openLiveTriviaApiService.registerUser(registerModel)

    fun login(): Observable<UserModel> = openLiveTriviaApiService.login()

    fun deleteUser(): Observable<MessageModel> = openLiveTriviaApiService.deleteUser()

    fun checkUsernameAvailability(username: String): Completable =
        openLiveTriviaApiService.checkUsernameAvailability(username)

    fun updateUserRights(userId: String, rightsLevel: Int): Observable<MessageModel> =
        openLiveTriviaApiService.updateUserRights(userId, rightsLevel)

    fun getLeaderboard(): Observable<PaginatedResponseModel<UserModel>> =
        openLiveTriviaApiService.getLeaderboard()

    fun getMe(): Observable<UserModel> = openLiveTriviaApiService.getMe()

    // Entry reports

    fun getReportedEntries(banned: Boolean? = null): Observable<PaginatedResponseModel<EntryReportModel>> =
        openLiveTriviaApiService.getReportedEntries(banned)

    fun banReportedEntry(reportId: String): Observable<MessageModel> =
        openLiveTriviaApiService.banReportedEntry(reportId)

    fun unbanReportedEntry(reportId: String): Observable<MessageModel> =
        openLiveTriviaApiService.unbanReportedEntry(reportId)

    fun dismissReportedEntry(reportId: String): Observable<MessageModel> =
        openLiveTriviaApiService.dismissReportedEntry(reportId)

    // System

    fun disconnectEveryone(): Observable<MessageModel> = openLiveTriviaApiService.disconnectEveryone()

    fun getSystemInfo(): Observable<SystemInfoModel> = openLiveTriviaApiService.getSystemInfo()
}