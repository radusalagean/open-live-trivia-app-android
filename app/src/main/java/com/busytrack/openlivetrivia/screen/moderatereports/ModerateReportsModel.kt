package com.busytrack.openlivetrivia.screen.moderatereports

import com.busytrack.openlivetrivia.generic.mvp.BaseModel
import com.busytrack.openlivetrivia.network.NetworkRepository
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import io.reactivex.Observable

class ModerateReportsModel(
    private val networkRepository: NetworkRepository
) : BaseModel<ModerateReportsViewModel>(), ModerateReportsMvp.Model {

    override fun initReportedEntries(): Observable<PaginatedResponseModel<EntryReportModel>> =
        networkRepository.getReportedEntries(false)
            .doOnNext {
                viewModel.clearReported()
                viewModel.reportedEntries.addAll(it.items)
                handleNextPage(false, it)
            }

    override fun initBannedEntries(): Observable<PaginatedResponseModel<EntryReportModel>> =
        networkRepository.getReportedEntries(true)
            .doOnNext {
                viewModel.clearBanned()
                viewModel.bannedEntries.addAll(it.items)
                handleNextPage(true, it)
            }

    override fun getNextReportedEntriesPage(): Observable<PaginatedResponseModel<EntryReportModel>> =
        networkRepository.getReportedEntries(false, viewModel.nextAvailablePageReportedEntries!!)
            .doOnNext {
                viewModel.reportedEntries.addAll(it.items)
                handleNextPage(false, it)
            }

    override fun getNextBannedEntriesPage(): Observable<PaginatedResponseModel<EntryReportModel>> =
        networkRepository.getReportedEntries(true, viewModel.nextAvailablePageBannedEntries!!)
            .doOnNext {
                viewModel.bannedEntries.addAll(it.items)
                handleNextPage(true, it)
            }

    override fun banEntry(reportId: String): Observable<MessageModel> =
        networkRepository.banReportedEntry(reportId)

    override fun unbanEntry(reportId: String): Observable<MessageModel> =
        networkRepository.unbanReportedEntry(reportId)

    override fun dismissReport(reportId: String): Observable<MessageModel> =
        networkRepository.dismissReportedEntry(reportId)

    private fun handleNextPage(banned: Boolean, response: PaginatedResponseModel<EntryReportModel>) {
        val nextPage = with(response) {
            if (page < pages) page + 1 else null
        }
        if (banned) {
            viewModel.nextAvailablePageBannedEntries = nextPage
        } else {
            viewModel.nextAvailablePageReportedEntries = nextPage
        }
    }
}