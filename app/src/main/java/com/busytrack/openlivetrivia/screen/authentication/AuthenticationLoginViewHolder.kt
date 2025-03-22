package com.busytrack.openlivetrivia.screen.authentication

import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.LayoutLogInBinding

class AuthenticationLoginViewHolder(
    val binding: LayoutLogInBinding,
    private val contract: AuthenticationContract
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        with(itemView) {
            binding.buttonLogIn.setOnClickListener {
                contract.onLoginPressed()
            }
            binding.privacyPolicyLink.setOnClickListener {
                contract.onPrivacyPolicyLinkPressed()
            }
        }
    }

    fun recycle() {
        with(itemView) {
            binding.buttonLogIn.setOnLongClickListener(null)
            binding.privacyPolicyLink.setOnClickListener(null)
        }
    }
}