package com.busytrack.openlivetrivia.screen.authentication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AuthenticationAdapter(
    private val contract: AuthenticationContract
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var loginViewHolder: AuthenticationLoginViewHolder? = null
        private set

    var registerViewHolder: AuthenticationRegisterViewHolder? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val pageType = AuthenticationPageType.values()[viewType]
        return LayoutInflater.from(parent.context).inflate(pageType.layoutId, parent, false).let {
            when(pageType) {
                AuthenticationPageType.LOG_IN ->
                    AuthenticationLoginViewHolder(it, contract).also { loginViewHolder = it }
                AuthenticationPageType.REGISTER ->
                    AuthenticationRegisterViewHolder(it, contract).also { registerViewHolder = it }
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

    override fun getItemCount() = AuthenticationPageType.values().size

    override fun getItemViewType(position: Int) = position
}