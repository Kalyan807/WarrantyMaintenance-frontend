package com.simats.warrantymaintenance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.warrantymaintenance.adapter.AssignedTaskTechnicianAdapter
import com.simats.warrantymaintenance.adapter.CompletedTaskAdapter
import com.simats.warrantymaintenance.adapter.TodaysScheduleAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.TechnicianDashboardData
import com.simats.warrantymaintenance.databinding.ActivityTechnicianDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TechnicianDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTechnicianDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.todaysScheduleRecycler.layoutManager = LinearLayoutManager(this)
        binding.assignedTasksRecycler.layoutManager = LinearLayoutManager(this)
        binding.completedTasksRecycler.layoutManager = LinearLayoutManager(this)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    // Already on the dashboard, do nothing
                    true
                }
                R.id.history -> {
                    startActivity(Intent(this, ServiceHistoryActivity::class.java))
                    true
                }
                R.id.alerts -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, TechnicianProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        ApiClient.instance.getTechnicianDashboardData().enqueue(object : Callback<TechnicianDashboardData> {
            override fun onResponse(call: Call<TechnicianDashboardData>, response: Response<TechnicianDashboardData>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        binding.assignedCount.text = data.assigned.toString()
                        binding.completedCount.text = data.completed.toString()
                        binding.todayCount.text = data.today.toString()

                        binding.todaysScheduleRecycler.adapter = TodaysScheduleAdapter(data.todaysSchedule)
                        binding.assignedTasksRecycler.adapter = AssignedTaskTechnicianAdapter(data.assignedTasks)
                        binding.completedTasksRecycler.adapter = CompletedTaskAdapter(data.completedTasks)
                    }
                } else {
                    Toast.makeText(this@TechnicianDashboardActivity, "Failed to load dashboard data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TechnicianDashboardData>, t: Throwable) {
                Toast.makeText(this@TechnicianDashboardActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
