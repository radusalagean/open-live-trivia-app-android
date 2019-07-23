package com.busytrack.openlivetrivia.screen.game

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import kotlinx.android.synthetic.main.layout_attempt.view.*

abstract class GameAttemptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var defaultBackground: Drawable = itemView.attempt_frame_layout.background
    private var defaultUsernameTextColor: ColorStateList = itemView.attempt_text_view_username.textColors
    private var defaultAttemptTextColor: ColorStateList = itemView.attempt_text_view_attempt.textColors

    protected fun switchToCorrectAnswerState() {
        itemView.apply {
            attempt_frame_layout.background = ResourcesCompat.getDrawable(
                itemView.resources,
                R.drawable.layout_correct_attempt_background,
                null
            )
            attempt_text_view_username.setTextColor(ResourcesCompat.getColor(
                resources,
                R.color.colorWhite,
                null
            ))
            attempt_text_view_attempt.setTextColor(ResourcesCompat.getColor(
                itemView.resources,
                R.color.colorWhite,
                null
            ))
        }
    }

    protected fun switchToDefaultState() {
        itemView.attempt_frame_layout.background = defaultBackground
        itemView.attempt_text_view_username.setTextColor(defaultUsernameTextColor)
        itemView.attempt_text_view_attempt.setTextColor(defaultAttemptTextColor)
    }
}