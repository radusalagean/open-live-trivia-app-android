package com.busytrack.openlivetrivia.screen.moderatereports

import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import io.reactivex.Observable

interface ModerateReportsMvp {

    interface Model : BaseMvp.Model<ModerateReportsViewModel> {
        fun initReportedEntries(): Observable<PaginatedResponseModel<EntryReportModel>>
        fun initBannedEntries(): Observable<PaginatedResponseModel<EntryReportModel>>
        fun getNextReportedEntriesPage(): Observable<PaginatedResponseModel<EntryReportModel>>
        fun getNextBannedEntriesPage(): Observable<PaginatedResponseModel<EntryReportModel>>
        fun banEntry(reportId: String): Observable<MessageModel>
        fun unbanEntry(reportId: String): Observable<MessageModel>
        fun dismissReport(reportId: String): Observable<MessageModel>
    }

    interface View : BaseMvp.View {
        fun setReportedEntriesRefreshingIndicator(refreshing: Boolean)
        fun setBannedEntriesRefreshingIndicator(refreshing: Boolean)
        fun updateReportedEntries(reportedEntries: List<EntryReportModel>)
        fun updateBannedEntries(bannedEntries: List<EntryReportModel>)
        fun updateReportedEntriesLoadMoreState(loading: Boolean)
        fun updateBannedEntriesLoadMoreState(loading: Boolean)
    }

    interface Presenter : BaseMvp.Presenter<View> {
        var refreshingReportedEntries: Boolean
        var refreshingBannedEntries: Boolean
        var loadingMoreReportedEntries: Boolean
        var loadingMoreBannedEntries: Boolean
        fun requestReportedEntries(invalidate: Boolean = false)
        fun requestBannedEntries(invalidate: Boolean = false)
        fun onReportedEntriesScrollThresholdReached()
        fun onBannedEntriesScrollThresholdReached()
        fun banEntry(entry: EntryReportModel)
        fun unbanEntry(entry: EntryReportModel)
        fun dismissReport(entry: EntryReportModel)
    }
}