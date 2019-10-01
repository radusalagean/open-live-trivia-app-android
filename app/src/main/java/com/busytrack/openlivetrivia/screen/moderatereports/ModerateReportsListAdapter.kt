package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.recyclerview.DynamicLoadAdapter
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import kotlinx.android.synthetic.main.item_reported_entry.view.*
import kotlinx.android.synthetic.main.layout_base_entry.view.*

class ModerateReportsListAdapter( // TODO test
    private val reports: ArrayList<EntryReportModel?> = arrayListOf(),
    private val contract: ModerateReportsItemContract
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    DynamicLoadAdapter<EntryReportModel> {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false).let {
            when(viewType) {
                R.layout.item_reported_entry -> {
                    ModerateReportsListViewHolder(it).apply {
                        // Hide game-specific views
                        itemView.group_game_specific_views.visibility = View.GONE
                        itemView.button_ban_entry.setOnClickListener {
                            contract.onBanClicked(reports[adapterPosition]!!)
                        }
                        itemView.button_unban_entry.setOnClickListener {
                            contract.onUnbanClicked(reports[adapterPosition]!!)
                        }
                        itemView.button_dismiss_report.setOnClickListener {
                            contract.onDismissClicked(reports[adapterPosition]!!)
                        }
                    }
                }
                else -> object : RecyclerView.ViewHolder(it){}
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ModerateReportsListViewHolder) {
            holder.bind(reports[position]!!)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is ModerateReportsListViewHolder) {
            holder.recycle()
        }
    }

    override fun getItemCount() = reports.size

    override fun getItemViewType(position: Int): Int {
        return when(reports[position]) {
            is EntryReportModel -> R.layout.item_reported_entry
            else -> R.layout.item_reported_entry_loading
        }
    }

    override fun getItemId(position: Int) =
        reports[position]?.reportId?.hashCode()?.toLong() ?: 0L

    // DynamicLoadAdapter

    override fun showLoadingPlaceholder() {
        reports.add(null)
        notifyDataSetChanged()
    }

    override fun hideLoadingPlaceholder() {
        reports.remove(null)
        notifyDataSetChanged()
    }

    override fun setList(list: List<EntryReportModel>) {
        reports.clear()
        reports.addAll(list)
        notifyDataSetChanged()
    }

    override fun clearList() {
        reports.clear()
        notifyDataSetChanged()
    }
}