package com.busytrack.openlivetrivia.screen.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.ItemAttemptOwnBinding
import com.busytrack.openlivetrivia.databinding.ItemAttemptPeerBinding
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel

class GameAttemptsAdapter(
    private val contract: GameAttemptContract,
    private val attempts: ArrayList<AttemptModel>,
    private val authenticatedUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == ViewType.ATTEMPT_TYPE_OWN.id) {
            val binding = ItemAttemptOwnBinding.inflate(
                layoutInflater, parent, false
            )
            GameOwnAttemptViewHolder(binding)
        } else {
            val binding = ItemAttemptPeerBinding.inflate(
                layoutInflater, parent, false
            )
            GamePeerAttemptViewHolder(binding)
        }
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
        if (attempts[position].userId == authenticatedUserId)
            ViewType.ATTEMPT_TYPE_OWN.id
        else
            ViewType.ATTEMPT_TYPE_PEER.id

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

    enum class ViewType(val id: Int) {
        ATTEMPT_TYPE_OWN(0),
        ATTEMPT_TYPE_PEER(1)
    }
}