package com.busytrack.openlivetrivia.screen.moderatereports

import com.busytrack.openlivetrivia.generic.viewmodel.BaseViewModel
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel

class ModerateReportsViewModel : BaseViewModel() {

    var reportedEntries = arrayListOf<EntryReportModel>()
    var bannedEntries = arrayListOf<EntryReportModel>()
    var nextAvailablePageReportedEntries: Int? = null
    var nextAvailablePageBannedEntries: Int? = null

    fun clearReported() {
        reportedEntries.clear()
        nextAvailablePageReportedEntries = null
    }

    fun clearBanned() {
        bannedEntries.clear()
        nextAvailablePageBannedEntries = null
    }
}