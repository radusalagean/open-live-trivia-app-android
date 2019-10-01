package com.busytrack.openlivetrivia.screen.game

import android.view.View
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel
import kotlinx.android.synthetic.main.item_attempt_peer.view.*
import kotlinx.android.synthetic.main.layout_attempt.view.*

class GamePeerAttemptViewHolder(itemView: View) : GameAttemptViewHolder(itemView) { // TODO test
    fun bind(model: AttemptModel) {
        with(itemView) {
            attempt_text_view_username.visibility = View.VISIBLE
            attempt_text_view_username.text = model.username
            attempt_text_view_attempt.text = model.message
            Glide.with(this)
                .load(UserModel.getThumbnailPath(model.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(attempt_image_view_profile)
        }
        if (model.correct) {
            switchToCorrectAnswerState()
        }
    }

    fun recycle() {
        with(itemView) {
            attempt_text_view_username.text = null
            attempt_text_view_attempt.text = null
            Glide.with(this.context.applicationContext).clear(attempt_image_view_profile)
            attempt_image_view_profile.setImageDrawable(null)
        }
        switchToDefaultState()
    }
}