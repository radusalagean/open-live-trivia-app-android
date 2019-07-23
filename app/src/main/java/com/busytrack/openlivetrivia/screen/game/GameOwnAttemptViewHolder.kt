package com.busytrack.openlivetrivia.screen.game

import android.view.View
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel
import kotlinx.android.synthetic.main.layout_attempt.view.*

class GameOwnAttemptViewHolder(itemView: View) : GameAttemptViewHolder(itemView) {
    fun bind(model: AttemptModel) {
        itemView.attempt_text_view_attempt.text = model.message
        if (model.correct) {
            switchToCorrectAnswerState()
        }
    }

    fun recycle() {
        itemView.attempt_text_view_attempt.text = null
        switchToDefaultState()
    }
}