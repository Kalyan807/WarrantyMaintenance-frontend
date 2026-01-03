package com.simats.warrantymaintenance.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.AssignTechnicianActivity
import com.simats.warrantymaintenance.R
import com.simats.warrantymaintenance.data.Issue
import com.simats.warrantymaintenance.databinding.ItemIssueBinding

class IssuesAdapter(private val issues: List<Issue>, private val onAssignClick: (Issue) -> Unit) : RecyclerView.Adapter<IssuesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding = ItemIssueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(issues[position])
    }

    override fun getItemCount() = issues.size

    inner class ViewHolder(private val binding: ItemIssueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(issue: Issue) {
            binding.applianceName.text = issue.applianceName
            binding.issueDescription.text = issue.issueDescription
            binding.priority.text = issue.priority
            binding.statusChip.text = issue.status
            binding.reportedBy.text = "Reported by ${issue.reportedBy} on ${issue.reportedDate}"

            if (issue.status == "Pending") {
                binding.assignButton.visibility = View.VISIBLE
                binding.assignButton.setOnClickListener {
                    onAssignClick(issue)
                }
            } else {
                binding.assignButton.visibility = View.GONE
            }
        }
    }
}
