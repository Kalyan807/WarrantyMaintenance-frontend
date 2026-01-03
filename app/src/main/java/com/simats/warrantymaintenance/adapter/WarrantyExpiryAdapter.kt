package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.data.WarrantyExpiry
import com.simats.warrantymaintenance.databinding.ItemWarrantyExpiryBinding

class WarrantyExpiryAdapter(private val warranties: List<WarrantyExpiry>) :
    RecyclerView.Adapter<WarrantyExpiryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemWarrantyExpiryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(warranties[position])
    }

    override fun getItemCount() = warranties.size

    inner class ViewHolder(private val binding: ItemWarrantyExpiryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(warranty: WarrantyExpiry) {
            binding.applianceName.text = warranty.applianceName
            binding.ownerName.text = "Owner: ${warranty.ownerName}"
            binding.expiresText.text = "Expires: ${warranty.expiryDate}"
            binding.daysLeftText.text = "${warranty.daysLeft} days left"
            binding.daysLeftChip.text = "${warranty.daysLeft}d"
        }
    }
}
