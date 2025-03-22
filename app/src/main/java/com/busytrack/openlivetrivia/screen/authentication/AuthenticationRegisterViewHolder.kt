package com.busytrack.openlivetrivia.screen.authentication

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.LayoutRegisterBinding

class AuthenticationRegisterViewHolder(
    val binding: LayoutRegisterBinding,
    private val contract: AuthenticationContract
) : RecyclerView.ViewHolder(binding.root) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            contract.onUsernameChanged(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    fun bind() {
        binding.run {
            editTextUsername.addTextChangedListener(textWatcher)
            buttonRegister.setOnClickListener {
                contract.onRegisterPressed(editTextUsername.text.trim().toString())
            }
            linkSelectAnotherAccount.setOnClickListener {
                contract.onChangeAccountPressed()
            }
        }
    }

    fun recycle() {
        binding.run {
            editTextUsername.removeTextChangedListener(textWatcher)
            buttonRegister.setOnClickListener(null)
            linkSelectAnotherAccount.setOnClickListener(null)
        }
    }
}