package com.busytrack.openlivetrivia.screen.authentication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_log_in.view.*

class AuthenticationLoginViewHolder(
    view: View,
    private val contract: AuthenticationContract
) : RecyclerView.ViewHolder(view) {

    fun bind() {
        with(itemView) {
            button_log_in.setOnClickListener {
                contract.onLoginPressed()
            }
            privacy_policy_link.setOnClickListener {
                contract.onPrivacyPolicyLinkPressed()
            }
        }
    }

    fun recycle() {
        with(itemView) {
            button_log_in.setOnLongClickListener(null)
            privacy_policy_link.setOnClickListener(null)
        }
    }
}