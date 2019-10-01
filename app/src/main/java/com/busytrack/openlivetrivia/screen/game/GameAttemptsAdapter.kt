package com.busytrack.openlivetrivia.screen.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel

private const val ATTEMPT_TYPE_OWN = 0
private const val ATTEMPT_TYPE_PEER = 1

class GameAttemptsAdapter( // TODO test
    private val contract: GameAttemptContract,
    private val attempts: ArrayList<AttemptModel>,
    private val authenticatedUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            if (viewType == ATTEMPT_TYPE_OWN) R.layout.item_attempt_own else R.layout.item_attempt_peer,
            parent,
            false
        )
        return if (viewType == ATTEMPT_TYPE_OWN) GameOwnAttemptViewHolder(view) else GamePeerAttemptViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GameOwnAttemptViewHolder -> holder.bind(attempts[position], contract)
            is GamePeerAttemptViewHolder -> holder.bind(attempts[position])
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is GameOwnAttemptViewHolder -> holder.recycle()
            is GamePeerAttemptViewHolder -> holder.recycle()
        }
    }

    override fun getItemCount() = attempts.size

    override fun getItemViewType(position: Int) =
        if (attempts[position].userId == authenticatedUserId) ATTEMPT_TYPE_OWN else ATTEMPT_TYPE_PEER

    fun addAttempt(model: AttemptModel) {
        attempts.add(model)
        notifyItemInserted(attempts.size - 1)
    }

    fun initializeAttempts(models: List<AttemptModel>) {
        attempts.clear()
        attempts.addAll(models)
        notifyDataSetChanged()
    }

    fun clearAttempts() {
        attempts.clear()
        notifyDataSetChanged()
    }
}