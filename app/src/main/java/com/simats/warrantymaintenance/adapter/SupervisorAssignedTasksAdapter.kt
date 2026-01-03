package com.simats.warrantymaintenance.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.TaskDetailsActivity
import com.simats.warrantymaintenance.data.AssignedTask
import com.simats.warrantymaintenance.databinding.ItemAssignedTaskBinding

class SupervisorAssignedTasksAdapter(private val tasks: List<AssignedTask>) :
    RecyclerView.Adapter<SupervisorAssignedTasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemAssignedTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    inner class ViewHolder(private val binding: ItemAssignedTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: AssignedTask) {
            binding.taskTitle.text = task.applianceName
            binding.taskDescription.text = task.issueDescription
            binding.technicianName.text = "Technician: Unassigned" // Placeholder

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, TaskDetailsActivity::class.java).apply {
                    putExtra("TASK_ID", task.id) // Assuming 'id' is a property of your AssignedTask data class
                }
                context.startActivity(intent)
            }
        }
    }
}
