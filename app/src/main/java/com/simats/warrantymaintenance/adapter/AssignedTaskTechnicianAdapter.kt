package com.simats.warrantymaintenance.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.TaskDetailsActivity
import com.simats.warrantymaintenance.data.AssignedTask
import com.simats.warrantymaintenance.databinding.ItemAssignedTaskTechnicianBinding

class AssignedTaskTechnicianAdapter(private val tasks: List<AssignedTask>) :
    RecyclerView.Adapter<AssignedTaskTechnicianAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemAssignedTaskTechnicianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    inner class ViewHolder(private val binding: ItemAssignedTaskTechnicianBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: AssignedTask) {
            binding.applianceName.text = task.applianceName
            binding.issueDescription.text = task.issueDescription
            binding.address.text = task.address
            binding.dateTime.text = task.dateTime

            binding.viewDetailsButton.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, TaskDetailsActivity::class.java).apply {
                    putExtra("TASK_ID", task.id)
                }
                context.startActivity(intent)
            }
        }
    }
}
