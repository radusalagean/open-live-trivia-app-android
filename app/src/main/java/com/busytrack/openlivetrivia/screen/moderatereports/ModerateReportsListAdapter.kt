package com.busytrack.openlivetrivia.screen.moderatereports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.ItemReportedEntryBinding
import com.busytrack.openlivetrivia.databinding.ItemReportedEntryLoadingBinding
import com.busytrack.openlivetrivia.generic.recyclerview.DynamicLoadAdapter
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel

class ModerateReportsListAdapter(
    private val reports: ArrayList<EntryReportModel?> = arrayListOf(),
    private val contract: ModerateReportsItemContract
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    DynamicLoadAdapter<EntryReportModel> {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = when (viewType) {
            ViewType.LOADING.id -> {
                val binding = ItemReportedEntryLoadingBinding.inflate(
                    layoutInflater, parent, false
                )
                object : RecyclerView.ViewHolder(binding.root){}
            }
            ViewType.REPORTED_ENTRY.id -> {
                val binding = ItemReportedEntryBinding.inflate(
                    layoutInflater, parent, false
                )
                ModerateReportsListViewHolder(binding).apply {
                    // Hide game-specific views
                    binding.layoutBaseEntry.groupGameSpecificViews.visibility = View.GONE
                    binding.buttonBanEntry.setOnClickListener {
                        contract.onBanClicked(reports[adapterPosition]!!)
                    }
                    binding.buttonUnbanEntry.setOnClickListener {
                        contract.onUnbanClicked(reports[adapterPosition]!!)
                    }
                    binding.buttonDismissReport.setOnClickListener {
                        contract.onDismissClicked(reports[adapterPosition]!!)
                    }
                }
            }
            else -> null
        }
        return requireNotNull(viewHolder)
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
            is EntryReportModel -> ViewType.REPORTED_ENTRY.id
            else -> ViewType.LOADING.id
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

    enum class ViewType(val id: Int) {
        LOADING(0),
        REPORTED_ENTRY(1)
    }
}