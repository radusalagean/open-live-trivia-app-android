package com.busytrack.openlivetrivia.screen.authentication

import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.LayoutLogInBinding

class AuthenticationLoginViewHolder(
    val binding: LayoutLogInBinding,
    private val contract: AuthenticationContract
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        with(binding) {
            buttonLogIn.setOnClickListener {
                contract.onLoginPressed()
            }
            privacyPolicyLink.setOnClickListener {
                contract.onPrivacyPolicyLinkPressed()
            }
        }
    }

    fun recycle() {
        with(binding) {
            buttonLogIn.setOnLongClickListener(null)
            privacyPolicyLink.setOnClickListener(null)
        }
    }
}