package com.kkagurazaka.redukt.sample.presentation.common

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindingViewHolder(private val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(variableId: Int, value: Any?) {
        binding.setVariable(variableId, value)
        binding.executePendingBindings()
    }
}
