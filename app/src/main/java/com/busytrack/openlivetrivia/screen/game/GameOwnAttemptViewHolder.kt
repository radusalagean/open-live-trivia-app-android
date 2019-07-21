package com.busytrack.openlivetrivia.screen.game

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel
import kotlinx.android.synthetic.main.layout_attempt.view.*

class GameOwnAttemptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(model: AttemptModel) {
        itemView.attempt_text_view_attempt.text = model.message
    }

    fun recycle() {
        itemView.attempt_text_view_attempt.text = null
    }
}