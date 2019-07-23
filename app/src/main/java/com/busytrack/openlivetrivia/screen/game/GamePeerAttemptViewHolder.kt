package com.busytrack.openlivetrivia.screen.game

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.busytrack.openlivetriviainterface.USER_THUMBNAILS_EXTENSION
import com.busytrack.openlivetriviainterface.USER_THUMBNAILS_PATH
import com.busytrack.openlivetriviainterface.socket.model.AttemptModel
import kotlinx.android.synthetic.main.item_attempt_peer.view.*
import kotlinx.android.synthetic.main.layout_attempt.view.*

class GamePeerAttemptViewHolder(itemView: View) : GameAttemptViewHolder(itemView) {
    fun bind(model: AttemptModel) {
        with(itemView) {
            attempt_text_view_username.visibility = View.VISIBLE
            attempt_text_view_username.text = model.username
            attempt_text_view_attempt.text = model.message
            Glide.with(this)
                .load(USER_THUMBNAILS_PATH + model.userId + USER_THUMBNAILS_EXTENSION)
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
            Glide.with(this).clear(attempt_image_view_profile)
            attempt_image_view_profile.setImageDrawable(null)
        }
        switchToDefaultState()
    }
}