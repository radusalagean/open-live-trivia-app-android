package com.busytrack.openlivetriviainterface.rest

import com.busytrack.openlivetriviainterface.rest.model.*
import com.busytrack.openlivetriviainterface.socket.model.OutgoingAuthenticationModel
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*

interface OpenLiveTriviaApiService {

    // Users

    @POST("user/register")
    fun registerUser(@Body authenticationModel: OutgoingAuthenticationModel): Observable<UserModel>

    @POST("user/login")
    fun login(): Observable<UserModel>

    @DELETE("user/delete")
    fun deleteUser(): Observable<MessageModel>

    @GET("user/availability/{username}")
    fun checkUsernameAvailability(@Path("username") username: String): Completable

    @PUT("user/rights/{user_id}/{rights_level}")
    fun updateUserRights(
        @Path("user_id") userId: String,
        @Path("rights_level") rightsLevel: Int
    ): Observable<MessageModel>

    @GET("user/leaderboard")
    fun getLeaderboard(): Observable<PaginatedResponseModel<UserModel>>

    // Entry reports

    @GET("reported_entry/get_reports")
    fun getReportedEntries(
        @Query("banned") banned: Boolean? = null
    ): Observable<PaginatedResponseModel<EntryReportModel>>

    @PUT("reported_entry/ban/{report_id}")
    fun banReportedEntry(@Path("report_id") reportId: String): Observable<MessageModel>

    @PUT("reported_entry/unban/{report_id}")
    fun unbanReportedEntry(@Path("report_id") reportId: String): Observable<MessageModel>

    @PUT("reported_entry/dismiss/{report_id}")
    fun dismissReportedEntry(@Path("report_id") reportId: String): Observable<MessageModel>

    // System

    @POST("system/disconnect_everyone")
    fun disconnectEveryone(): Observable<MessageModel>

    @GET("system/info")
    fun getSystemInfo(): Observable<SystemInfoModel>
}