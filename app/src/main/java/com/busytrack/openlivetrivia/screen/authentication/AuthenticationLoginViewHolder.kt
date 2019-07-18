package com.busytrack.openlivetrivia.screen.authentication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_log_in.view.*

class AuthenticationLoginViewHolder(
    view: View,
    private val contract: AuthenticationContract
) : RecyclerView.ViewHolder(view) {

    fun bind() {
        itemView.button_log_in.setOnClickListener {
            contract.onLoginPressed()
        }
    }

    fun recycle() {
        itemView.setOnLongClickListener(null)
    }
}