package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.R
import com.simats.warrantymaintenance.data.Appliance
import com.simats.warrantymaintenance.databinding.ItemApplianceBinding

class AppliancesAdapter(private val appliances: List<Appliance>) :
    RecyclerView.Adapter<AppliancesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemApplianceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appliances[position])
    }

    override fun getItemCount() = appliances.size

    inner class ViewHolder(private val binding: ItemApplianceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appliance: Appliance) {
            binding.applianceName.text = appliance.name
            binding.applianceType.text = appliance.type
            binding.warrantyDaysLeft.text = "${appliance.warrantyDaysLeft} days"

            val imageResId = when (appliance.type) {
                "AC" -> R.drawable.ic_ac
                "Refrigerator" -> R.drawable.ic_refrigerator
                "TV" -> R.drawable.ic_tv
                "Washing Machine" -> R.drawable.ic_washing_machine
                else -> R.drawable.ic_appliance
            }
            binding.applianceImage.setImageResource(imageResId)
        }
    }
}
