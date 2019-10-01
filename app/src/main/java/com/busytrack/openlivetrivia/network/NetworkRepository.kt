package com.busytrack.openlivetrivia.network

import com.busytrack.openlivetriviainterface.rest.OpenLiveTriviaApiService
import com.busytrack.openlivetriviainterface.rest.model.*

class NetworkRepository(private val openLiveTriviaApiService: OpenLiveTriviaApiService) { // TODO test

    // Users

    fun registerUser(registerModel: OutgoingRegisterModel) =
        openLiveTriviaApiService.registerUser(registerModel)

    fun login() = openLiveTriviaApiService.login()

    fun deleteUser() = openLiveTriviaApiService.deleteUser()

    fun checkUsernameAvailability(username: String) =
        openLiveTriviaApiService.checkUsernameAvailability(username)

    fun updateUserRights(userId: String, rightsLevel: Int) =
        openLiveTriviaApiService.updateUserRights(userId, rightsLevel)

    fun getLeaderboard(page: Int = 1) =
        openLiveTriviaApiService.getLeaderboard(page)

    fun getMe() = openLiveTriviaApiService.getMe()

    // Entry reports

    fun getReportedEntries(banned: Boolean? = null, page: Int = 1) =
        openLiveTriviaApiService.getReportedEntries(banned, page)

    fun banReportedEntry(reportId: String) =
        openLiveTriviaApiService.banReportedEntry(reportId)

    fun unbanReportedEntry(reportId: String) =
        openLiveTriviaApiService.unbanReportedEntry(reportId)

    fun dismissReportedEntry(reportId: String) =
        openLiveTriviaApiService.dismissReportedEntry(reportId)

    // System

    fun disconnectEveryone() = openLiveTriviaApiService.disconnectEveryone()

    fun getSystemInfo() = openLiveTriviaApiService.getSystemInfo()
}