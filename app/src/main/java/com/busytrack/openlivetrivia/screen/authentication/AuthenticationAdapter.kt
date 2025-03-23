package com.busytrack.openlivetrivia.screen.authentication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busytrack.openlivetrivia.databinding.LayoutLogInBinding
import com.busytrack.openlivetrivia.databinding.LayoutRegisterBinding

class AuthenticationAdapter(
    private val contract: AuthenticationContract
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var loginViewHolder: AuthenticationLoginViewHolder? = null
        private set

    var registerViewHolder: AuthenticationRegisterViewHolder? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val pageType = AuthenticationPageType.entries[viewType]
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(pageType) {
            AuthenticationPageType.LOG_IN -> {
                val binding = LayoutLogInBinding.inflate(layoutInflater, parent, false)
                AuthenticationLoginViewHolder(binding, contract).also { loginViewHolder = it }
            }
            AuthenticationPageType.REGISTER -> {
                val binding = LayoutRegisterBinding.inflate(layoutInflater, parent, false)
                AuthenticationRegisterViewHolder(binding, contract).also { registerViewHolder = it }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AuthenticationLoginViewHolder -> holder.bind()
            is AuthenticationRegisterViewHolder -> holder.bind()
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when(holder) {
            is AuthenticationLoginViewHolder -> holder.recycle()
            is AuthenticationRegisterViewHolder -> holder.recycle()
        }
    }

    override fun getItemCount() = AuthenticationPageType.entries.size

    override fun getItemViewType(position: Int) = position
}