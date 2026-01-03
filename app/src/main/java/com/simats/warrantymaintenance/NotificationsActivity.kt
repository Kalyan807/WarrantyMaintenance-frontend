package com.simats.warrantymaintenance

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.warrantymaintenance.adapter.NotificationsAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.NotificationsResponse
import com.simats.warrantymaintenance.databinding.ActivityNotificationsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchNotifications()
    }

    private fun fetchNotifications() {
        ApiClient.instance.getNotifications().enqueue(object : Callback<NotificationsResponse> {
            override fun onResponse(call: Call<NotificationsResponse>, response: Response<NotificationsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        binding.unreadNotificationsText.text = "${data.unreadCount} unread notifications"
                        binding.notificationsRecyclerView.adapter = NotificationsAdapter(data.notifications)
                    }
                } else {
                    Toast.makeText(this@NotificationsActivity, "Failed to load notifications", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
                Toast.makeText(this@NotificationsActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
