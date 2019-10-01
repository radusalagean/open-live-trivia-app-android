package com.busytrack.openlivetrivia.screen.game

import android.view.View
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel
import kotlinx.android.synthetic.main.layout_attempt.view.*

class GameOwnAttemptViewHolder(itemView: View) : GameAttemptViewHolder(itemView) { // TODO test
    fun bind(model: AttemptModel, contract: GameAttemptContract) {
        itemView.setOnClickListener {
            contract.onAttemptClicked(model.message)
        }
        itemView.attempt_text_view_attempt.text = model.message
        if (model.correct) {
            switchToCorrectAnswerState()
        }
    }

    fun recycle() {
        itemView.setOnClickListener(null)
        itemView.attempt_text_view_attempt.text = null
        switchToDefaultState()
    }
}