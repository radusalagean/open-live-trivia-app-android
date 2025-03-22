package com.busytrack.openlivetrivia.screen.game

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.LayoutAttemptBinding

abstract class GameAttemptViewHolder(
    val layoutAttemptBinding: LayoutAttemptBinding,
    itemViewRoot: View
) : RecyclerView.ViewHolder(itemViewRoot) {
    private var defaultBackground: Drawable = layoutAttemptBinding
        .attemptFrameLayout.background
    private var defaultUsernameTextColor: ColorStateList = layoutAttemptBinding
        .attemptTextViewUsername.textColors
    private var defaultAttemptTextColor: ColorStateList = layoutAttemptBinding
        .attemptTextViewAttempt.textColors

    protected fun switchToCorrectAnswerState() {
        layoutAttemptBinding.apply {
            attemptFrameLayout.background = ResourcesCompat.getDrawable(
                itemView.resources,
                R.drawable.layout_correct_attempt_background,
                null
            )
            attemptTextViewUsername.setTextColor(ResourcesCompat.getColor(
                itemView.resources,
                R.color.colorWhite,
                null
            ))
            attemptTextViewAttempt.setTextColor(ResourcesCompat.getColor(
                itemView.resources,
                R.color.colorWhite,
                null
            ))
        }
    }

    protected fun switchToDefaultState() {
        layoutAttemptBinding.apply {
            attemptFrameLayout.background = defaultBackground
            attemptTextViewUsername.setTextColor(defaultUsernameTextColor)
            attemptTextViewAttempt.setTextColor(defaultAttemptTextColor)
        }
    }
}