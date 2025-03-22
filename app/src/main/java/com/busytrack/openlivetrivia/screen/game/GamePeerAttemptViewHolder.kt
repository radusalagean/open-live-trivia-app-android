package com.busytrack.openlivetrivia.screen.game

import android.view.View
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.ItemAttemptPeerBinding
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel

class GamePeerAttemptViewHolder(
    private val binding: ItemAttemptPeerBinding
) : GameAttemptViewHolder(
    layoutAttemptBinding = binding.attemptFrameLayout,
    itemViewRoot = binding.root
) {
    fun bind(model: AttemptModel) {
        with(binding.attemptFrameLayout) {
            attemptTextViewUsername.visibility = View.VISIBLE
            attemptTextViewUsername.text = model.username
            attemptTextViewAttempt.text = model.message
            Glide.with(binding.attemptImageViewProfile)
                .load(UserModel.getThumbnailPath(model.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(binding.attemptImageViewProfile)
        }
        if (model.correct) {
            switchToCorrectAnswerState()
        }
    }

    fun recycle() {
        with(binding.attemptFrameLayout) {
            attemptTextViewUsername.text = null
            attemptTextViewAttempt.text = null
            Glide.with(root.context.applicationContext)
                .clear(binding.attemptImageViewProfile)
            binding.attemptImageViewProfile.setImageDrawable(null)
        }
        switchToDefaultState()
    }
}