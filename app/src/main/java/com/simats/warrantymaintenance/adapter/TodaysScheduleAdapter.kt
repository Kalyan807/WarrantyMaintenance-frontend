package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.data.TodaysSchedule
import com.simats.warrantymaintenance.databinding.ItemTodaysScheduleBinding

class TodaysScheduleAdapter(private val tasks: List<TodaysSchedule>) :
    RecyclerView.Adapter<TodaysScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemTodaysScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    inner class ViewHolder(private val binding: ItemTodaysScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TodaysSchedule) {
            binding.applianceName.text = task.applianceName
            binding.issueDescription.text = task.issueDescription
            binding.customerName.text = task.customerName
            binding.address.text = task.address
            binding.time.text = task.time
        }
    }
}
