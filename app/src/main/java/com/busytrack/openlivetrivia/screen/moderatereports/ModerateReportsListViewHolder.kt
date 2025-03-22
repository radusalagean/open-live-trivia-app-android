package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.ItemReportedEntryBinding
import com.busytrack.openlivetrivia.extension.setVisibleHard
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel

class ModerateReportsListViewHolder(
    val binding: ItemReportedEntryBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val lastReportedPrefix = itemView.context.getString(R.string.last_reported_prefix)

    fun bind(model: EntryReportModel) {
        with(binding) {
            with(layoutBaseEntry) {
                textViewClueCategory.text = model.category?.also {
                    textViewClueCategory.visibility = View.VISIBLE
                }
                textViewClue.text = model.clue
                textViewAnswer.text = model.answer
            }
            textViewReporters.reporters = model.reporters.map { it.username }
            timeTextViewLastReported.setTimestamp(
                model.lastReported.time,
                lastReportedPrefix
            )
            buttonDismissReport.setVisibleHard(!model.banned)
            buttonBanEntry.setVisibleHard(!model.banned)
            buttonUnbanEntry.setVisibleHard(model.banned)
        }
    }

    fun recycle() {
        with(binding) {
            with(layoutBaseEntry) {
                textViewClueCategory.text = null
                textViewClueCategory.visibility = View.GONE
                textViewClue.text = null
                textViewAnswer.text = null
            }
            textViewReporters.reporters = null
            timeTextViewLastReported.clear()
            buttonDismissReport.setVisibleHard(false)
            buttonBanEntry.setVisibleHard(false)
            buttonUnbanEntry.setVisibleHard(false)
        }
    }
}