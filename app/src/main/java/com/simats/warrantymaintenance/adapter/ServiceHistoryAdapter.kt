package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.data.ServiceHistory
import com.simats.warrantymaintenance.databinding.ItemServiceHistoryBinding

class ServiceHistoryAdapter(private val history: List<ServiceHistory>) :
    RecyclerView.Adapter<ServiceHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemServiceHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(history[position])
    }

    override fun getItemCount() = history.size

    inner class ViewHolder(private val binding: ItemServiceHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ServiceHistory) {
            binding.applianceName.text = item.applianceName
            binding.customerName.text = item.customerName
            binding.date.text = item.date
        }
    }
}
