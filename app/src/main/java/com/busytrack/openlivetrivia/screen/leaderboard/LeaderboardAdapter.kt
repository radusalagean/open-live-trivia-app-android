package com.busytrack.openlivetrivia.screen.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import timber.log.Timber

class LeaderboardAdapter(
    private val leaderboardItemContract: LeaderboardItemContract,
    private var users: ArrayList<UserModel?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            else -> LeaderboardUserLoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Timber.d("onBindViewHolder($position)")
        when(holder) {
            is LeaderboardUserViewHolder -> {
                holder.bind(
                    users[position]!!,
                    position + 1
                )
            }
        }
    }

    override fun getItemCount() = users.size

    override fun getItemId(position: Int) = users[position]?.userId?.hashCode()?.toLong() ?: 0L

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        Timber.d("onViewRecycled()")
        when(holder) {
            is LeaderboardUserViewHolder -> holder.recycle()
        }
    }

    override fun getItemViewType(position: Int) =
        when(users[position]) {
            is UserModel -> R.layout.item_user
            else -> R.layout.item_user_loading
        }

    fun showLoadingPlaceholder() {
        users.add(null)
        notifyDataSetChanged()
    }

    fun hideLoadingPlaceholder() {
        users.remove(null)
        notifyDataSetChanged()
    }

    fun setUsers(users: List<UserModel>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    fun clear() {
        users.clear()
        notifyDataSetChanged()
    }
}