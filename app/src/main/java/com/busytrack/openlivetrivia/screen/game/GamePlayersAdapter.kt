package com.busytrack.openlivetrivia.screen.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.ItemUserCompactBinding
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.PlayerListModel

class GamePlayersAdapter(
    private val contract: GamePlayerContract,
    private val players: ArrayList<UserModel>
) : RecyclerView.Adapter<GamePlayerViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemUserCompactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let { GamePlayerViewHolder(it) }

    override fun onBindViewHolder(holder: GamePlayerViewHolder, position: Int) {
        holder.bind(players[position], contract)
    }

    override fun onViewRecycled(holder: GamePlayerViewHolder) {
        holder.recycle()
    }

    override fun getItemId(position: Int) = players[position].userId.hashCode().toLong()

    override fun getItemCount() = players.size

    fun initializePlayers(players: PlayerListModel) {
        this.players.clear()
        this.players.addAll(players.players)
        notifyDataSetChanged()
    }
}