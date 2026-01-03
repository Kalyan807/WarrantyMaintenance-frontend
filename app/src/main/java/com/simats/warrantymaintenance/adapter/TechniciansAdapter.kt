package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.data.Technician
import com.simats.warrantymaintenance.databinding.ItemTechnicianBinding

class TechniciansAdapter(private val technicians: List<Technician>) :
    RecyclerView.Adapter<TechniciansAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemTechnicianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(technicians[position])
    }

    override fun getItemCount() = technicians.size

    inner class ViewHolder(private val binding: ItemTechnicianBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(technician: Technician) {
            binding.technicianName.text = technician.name
            binding.technicianSpecialization.text = technician.specialization
            binding.technicianExperience.text = "${technician.experience} years experience"
            binding.status.text = technician.status
            binding.rating.text = technician.rating.toString()
        }
    }
}
