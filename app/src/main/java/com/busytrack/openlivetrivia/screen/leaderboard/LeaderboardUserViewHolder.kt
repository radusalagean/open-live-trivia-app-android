package com.busytrack.openlivetrivia.screen.leaderboard

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import kotlinx.android.synthetic.main.item_user.view.*

class LeaderboardUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(user: UserModel, position: Int) {
        with(itemView) {
            handlePodium(position)
            Glide.with(context)
                .load(UserModel.getThumbnailPath(user.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(user_image_view_profile)
            user_text_view_place.text = position.toString()
            user_text_view_username.text = user.username
            user_text_view_rights.rightsLevel = user.rights
            user_text_view_status.setPlaying(
                user.playing,
                if (!user.playing) user.lastSeen!!.time else null
            )
            user_text_view_coins.setCoins(user.coins!!)
        }
    }

    fun recycle() {
        with(itemView) {
            handlePodium()
            Glide.with(context.applicationContext).clear(user_image_view_profile)
            user_image_view_profile.setImageDrawable(null)
            user_text_view_place.text = null
            user_text_view_username.text = null
            user_text_view_rights.rightsLevel = null
            user_text_view_status.clear()
            user_text_view_coins.setCoins(0.0)
        }
    }

    private fun handlePodium(position: Int = 0) { // TODO test
        val background = when(position) {
            1 -> R.drawable.text_view_user_coin_indicator_gold_background
            2 -> R.drawable.text_view_user_coin_indicator_silver_background
            3 -> R.drawable.text_view_user_coin_indicator_bronze_background
            else -> R.drawable.text_view_user_coin_indicator_background
        }
        itemView.linear_layout_coins.background = ContextCompat.getDrawable(
            itemView.context,
            background
        )
        with(itemView.shimmer_layout_coins) {
            if (position in 1..3) showShimmer(true) else hideShimmer()
        }
    }
}