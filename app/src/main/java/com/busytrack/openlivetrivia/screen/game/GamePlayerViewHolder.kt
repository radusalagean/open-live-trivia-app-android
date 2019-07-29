package com.busytrack.openlivetrivia.screen.game

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import kotlinx.android.synthetic.main.item_user_compact.view.*

class GamePlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(user: UserModel, contract: GamePlayerContract) {
        with(itemView) {
            user_parent_layout.setOnLongClickListener {
                contract.onPlayerLongClicked(user)
                true
            }
            Glide.with(context)
                .load(UserModel.getThumbnailPath(user.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(user_image_view_profile)
            user_text_view_username.text = user.username
            user_text_view_rights.rightsLevel = user.rights
            user_text_view_status.setPlaying(true)
            user_text_view_coins.setCoins(user.coins!!)
        }
    }

    fun recycle() {
        with(itemView) {
            user_parent_layout.setOnLongClickListener(null)
            Glide.with(this.context.applicationContext).clear(user_image_view_profile)
            user_image_view_profile.setImageDrawable(null)
            user_text_view_username.text = null
            user_text_view_rights.rightsLevel = null
            user_text_view_status.clear()
            user_text_view_coins.setCoins(0.0)
        }
    }
}