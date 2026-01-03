package com.simats.warrantymaintenance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.warrantymaintenance.R
import com.simats.warrantymaintenance.data.Notification
import com.simats.warrantymaintenance.databinding.ItemNotificationBinding

class NotificationsAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount() = notifications.size

    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.notificationTitle.text = notification.title
            binding.notificationMessage.text = notification.message
            binding.notificationTime.text = notification.time

            binding.unreadDot.visibility = if (notification.isRead) View.INVISIBLE else View.VISIBLE

            val iconResId = when (notification.type) {
                "new_task" -> R.drawable.ic_new_task
                "rescheduled" -> R.drawable.ic_rescheduled
                "message" -> R.drawable.ic_message
                "approved" -> R.drawable.ic_approved
                else -> R.drawable.ic_alerts
            }
            binding.notificationIcon.setImageResource(iconResId)
        }
    }
}
