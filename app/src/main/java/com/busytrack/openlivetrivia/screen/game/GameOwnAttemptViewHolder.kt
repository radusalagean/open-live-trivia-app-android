package com.busytrack.openlivetrivia.screen.game

import com.busytrack.openlivetrivia.databinding.ItemAttemptOwnBinding
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel

class GameOwnAttemptViewHolder(
    private val binding: ItemAttemptOwnBinding
) : GameAttemptViewHolder(
    layoutAttemptBinding = binding.attemptFrameLayout,
    itemViewRoot = binding.root
) {
    fun bind(model: AttemptModel, contract: GameAttemptContract) {
        itemView.setOnClickListener {
            contract.onAttemptClicked(model.message)
        }
        binding.attemptFrameLayout.attemptTextViewAttempt.text = model.message
        if (model.correct) {
            switchToCorrectAnswerState()
        }
    }

    fun recycle() {
        itemView.setOnClickListener(null)
        binding.attemptFrameLayout.attemptTextViewAttempt.text = null
        switchToDefaultState()
    }
}