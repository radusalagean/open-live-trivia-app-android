package com.busytrack.openlivetrivia.screen.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.ItemUserBinding
import com.busytrack.openlivetrivia.databinding.ItemUserLoadingBinding
import com.busytrack.openlivetrivia.generic.recyclerview.DynamicLoadAdapter
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import timber.log.Timber

class LeaderboardAdapter(
    private val leaderboardItemContract: LeaderboardItemContract,
    private var users: ArrayList<UserModel?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    DynamicLoadAdapter<UserModel> {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Timber.d("onCreateViewHolder($viewType)")
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = when (viewType) {
            ViewType.LOADING.id -> {
                val binding = ItemUserLoadingBinding.inflate(layoutInflater, parent, false)
                object : RecyclerView.ViewHolder(binding.root){}
            }
            ViewType.USER.id -> {
                val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
                LeaderboardUserViewHolder(binding).also {
                    it.itemView.setOnLongClickListener { _ ->
                        leaderboardItemContract.onUserLongClicked(users[it.adapterPosition]!!)
                        true
                    }
                }
            }
            else -> null
        }
        return requireNotNull(viewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LeaderboardUserViewHolder) {
            holder.bind(
                users[position]!!,
                position + 1
            )
        }
    }

    override fun getItemCount() = users.size

    override fun getItemId(position: Int) = users[position]?.userId?.hashCode()?.toLong() ?: 0L

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is LeaderboardUserViewHolder) {
            holder.recycle()
        }
    }

    override fun getItemViewType(position: Int) =
        when(users[position]) {
            is UserModel -> ViewType.USER.id
            else -> ViewType.LOADING.id
        }

    // DynamicLoadAdapter

    override fun showLoadingPlaceholder() {
        users.add(null)
        notifyDataSetChanged()
    }

    override fun hideLoadingPlaceholder() {
        users.remove(null)
        notifyDataSetChanged()
    }

    override fun setList(list: List<UserModel>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }

    override fun clearList() {
        users.clear()
        notifyDataSetChanged()
    }

    enum class ViewType(val id: Int) {
        LOADING(0),
        USER(1)
    }
}