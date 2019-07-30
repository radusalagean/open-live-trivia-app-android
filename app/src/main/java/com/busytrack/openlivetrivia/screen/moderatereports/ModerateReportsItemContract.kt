package com.busytrack.openlivetrivia.screen.moderatereports

import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel

interface ModerateReportsItemContract {
    fun onBanClicked(model: EntryReportModel)
    fun onUnbanClicked(model: EntryReportModel)
    fun onDismissClicked(model: EntryReportModel)
}