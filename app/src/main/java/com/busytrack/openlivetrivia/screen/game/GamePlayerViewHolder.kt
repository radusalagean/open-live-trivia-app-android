package com.busytrack.openlivetrivia.screen.game

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.ItemUserCompactBinding
import com.busytrack.openlivetriviainterface.rest.model.UserModel

class GamePlayerViewHolder(val binding: ItemUserCompactBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserModel, contract: GamePlayerContract) {
        with(binding) {
            userParentLayout.setOnLongClickListener {
                contract.onPlayerLongClicked(user)
                true
            }
            Glide.with(userImageViewProfile)
                .load(UserModel.getThumbnailPath(user.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(userImageViewProfile)
            userTextViewUsername.text = user.username
            userTextViewRights.rightsLevel = user.rights
            userTextViewStatus.setPlaying(true)
            userTextViewCoins.setCoins(user.coins!!)
        }
    }

    fun recycle() {
        with(binding) {
            userParentLayout.setOnLongClickListener(null)
            Glide.with(root.context.applicationContext)
                .clear(userImageViewProfile)
            userImageViewProfile.setImageDrawable(null)
            userTextViewUsername.text = null
            userTextViewRights.rightsLevel = null
            userTextViewStatus.clear()
            userTextViewCoins.setCoins(0.0)
        }
    }
}