package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.data.CompletedTask
import com.simats.warrantymaintenance.databinding.ItemCompletedTaskBinding

class CompletedTaskAdapter(private val tasks: List<CompletedTask>) :
    RecyclerView.Adapter<CompletedTaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemCompletedTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    inner class ViewHolder(private val binding: ItemCompletedTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: CompletedTask) {
            binding.applianceName.text = task.applianceName
            binding.issueDescription.text = task.issueDescription
            binding.date.text = task.date
        }
    }
}
