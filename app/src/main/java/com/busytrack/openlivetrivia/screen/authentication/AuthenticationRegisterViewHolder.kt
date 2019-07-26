package com.busytrack.openlivetrivia.screen.authentication

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_register.view.*

class AuthenticationRegisterViewHolder(
    view: View,
    private val contract: AuthenticationContract
) : RecyclerView.ViewHolder(view) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            contract.onUsernameChanged(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    fun bind() {
        itemView.run {
            edit_text_username.addTextChangedListener(textWatcher)
            button_register.setOnClickListener {
                contract.onRegisterPressed(edit_text_username.text.trim().toString())
            }
            link_select_another_account.setOnClickListener {
                contract.onChangeAccountPressed()
            }
        }
    }

    fun recycle() {
        itemView.run {
            edit_text_username.removeTextChangedListener(textWatcher)
            button_register.setOnClickListener(null)
            link_select_another_account.setOnClickListener(null)
        }
    }
}