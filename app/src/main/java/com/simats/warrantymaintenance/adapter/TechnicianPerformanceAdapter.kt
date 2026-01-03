package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.data.TechnicianPerformance
import com.simats.warrantymaintenance.databinding.ItemTechnicianPerformanceBinding

class TechnicianPerformanceAdapter(private val technicians: List<TechnicianPerformance>) :
    RecyclerView.Adapter<TechnicianPerformanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemTechnicianPerformanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(technicians[position])
    }

    override fun getItemCount() = technicians.size

    inner class ViewHolder(private val binding: ItemTechnicianPerformanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(technician: TechnicianPerformance) {
            binding.technicianName.text = technician.name
            binding.tasksCompleted.text = "${technician.tasksCompleted} tasks completed"
            binding.rating.text = technician.rating.toString()
        }
    }
}
