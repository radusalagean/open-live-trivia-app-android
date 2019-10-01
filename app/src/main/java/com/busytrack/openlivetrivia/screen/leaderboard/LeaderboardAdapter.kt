package com.busytrack.openlivetrivia.screen.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.recyclerview.DynamicLoadAdapter
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import timber.log.Timber

class LeaderboardAdapter(
    private val leaderboardItemContract: LeaderboardItemContract,
    private var users: ArrayList<UserModel?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    DynamicLoadAdapter<UserModel> { // TODO test

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Timber.d("onCreateViewHolder($viewType)")
        val view = LayoutInflater.from(parent.context).inflate(
            viewType, parent, false)
        return when(viewType) {
            R.layout.item_user -> LeaderboardUserViewHolder(view).also {
                it.itemView.setOnLongClickListener { _ ->
                    leaderboardItemContract.onUserLongClicked(users[it.adapterPosition]!!)
                    true
                }
            }
            else -> object : RecyclerView.ViewHolder(view){}
        }
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
            is UserModel -> R.layout.item_user
            else -> R.layout.item_user_loading
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
}