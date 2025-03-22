package com.busytrack.openlivetrivia.screen.leaderboard

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.ItemUserBinding
import com.busytrack.openlivetriviainterface.rest.model.UserModel

class LeaderboardUserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserModel, position: Int) {
        with(binding) {
            handlePodium(position)
            Glide.with(userImageViewProfile)
                .load(UserModel.getThumbnailPath(user.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(userImageViewProfile)
            userTextViewPlace.text = position.toString()
            userTextViewUsername.text = user.username
            userTextViewRights.rightsLevel = user.rights
            userTextViewStatus.setPlaying(
                user.playing,
                if (!user.playing) user.lastSeen!!.time else null
            )
            userTextViewCoins.setCoins(user.coins!!)
        }
    }

    fun recycle() {
        with(binding) {
            handlePodium()
            Glide.with(root.context.applicationContext)
                .clear(userImageViewProfile)
            userImageViewProfile.setImageDrawable(null)
            userTextViewPlace.text = null
            userTextViewUsername.text = null
            userTextViewRights.rightsLevel = null
            userTextViewStatus.clear()
            userTextViewCoins.setCoins(0.0)
        }
    }

    private fun handlePodium(position: Int = 0) {
        val background = when(position) {
            1 -> R.drawable.text_view_user_coin_indicator_gold_background
            2 -> R.drawable.text_view_user_coin_indicator_silver_background
            3 -> R.drawable.text_view_user_coin_indicator_bronze_background
            else -> R.drawable.text_view_user_coin_indicator_background
        }
        binding.linearLayoutCoins.background = ContextCompat.getDrawable(
            itemView.context,
            background
        )
        with(binding.shimmerLayoutCoins) {
            if (position in 1..3) showShimmer(true) else hideShimmer()
        }
    }
}