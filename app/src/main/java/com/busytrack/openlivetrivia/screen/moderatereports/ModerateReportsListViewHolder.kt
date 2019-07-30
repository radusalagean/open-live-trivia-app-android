package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.extension.setVisibleHard
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import kotlinx.android.synthetic.main.item_reported_entry.view.*
import kotlinx.android.synthetic.main.layout_base_entry.view.*

class ModerateReportsListViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val lastReportedPrefix = itemView.context.getString(R.string.last_reported_prefix)

    fun bind(model: EntryReportModel) {
        with(itemView) {
            text_view_clue_category.text = model.category?.also {
                text_view_clue_category.visibility = View.VISIBLE
            }
            text_view_clue_category.text = model.category
            text_view_clue.text = model.clue
            text_view_answer.text = model.answer
            text_view_reporters.reporters = model.reporters.map { it.username }
            time_text_view_last_reported.setTimestamp(
                model.lastReported.time,
                lastReportedPrefix
            )
            button_dismiss_report.setVisibleHard(!model.banned)
            button_ban_entry.setVisibleHard(!model.banned)
            button_unban_entry.setVisibleHard(model.banned)
        }
    }

    fun recycle() {
        with(itemView) {
            text_view_clue_category.text = null
            text_view_clue_category.visibility = View.GONE
            text_view_clue.text = null
            text_view_answer.text = null
            text_view_reporters.reporters = null
            time_text_view_last_reported.clear()
            button_dismiss_report.setVisibleHard(false)
            button_ban_entry.setVisibleHard(false)
            button_unban_entry.setVisibleHard(false)
        }
    }
}