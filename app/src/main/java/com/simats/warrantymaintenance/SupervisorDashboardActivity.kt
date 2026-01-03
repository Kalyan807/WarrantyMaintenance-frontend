package com.simats.warrantymaintenance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.adapter.SupervisorAssignedTasksAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.DashboardSummary
import com.simats.warrantymaintenance.databinding.ActivitySupervisorDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupervisorDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupervisorDashboardBinding

    private val addTechnicianLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            fetchDashboardSummary()
        }
    }

    private val addWarrantyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            fetchDashboardSummary()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupervisorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.addTechnicianCard.setOnClickListener {
            val intent = Intent(this, AddTechnicianActivity::class.java)
            addTechnicianLauncher.launch(intent)
        }

        binding.addWarrantyCard.setOnClickListener {
            val intent = Intent(this, AddWarrantyActivity::class.java)
            addWarrantyLauncher.launch(intent)
        }

        binding.viewIssuesCard.setOnClickListener {
            startActivity(Intent(this, ViewIssuesActivity::class.java))
        }

        binding.analyticsCard.setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }

        binding.warrantyExpiryCard.setOnClickListener {
            startActivity(Intent(this, WarrantyExpiryActivity::class.java))
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.technicians -> {
                    startActivity(Intent(this, TechniciansActivity::class.java))
                    true
                }
                R.id.appliances -> {
                    startActivity(Intent(this, WarrantyExpiryActivity::class.java))
                    true
                }
                R.id.issues -> {
                    startActivity(Intent(this, ViewIssuesActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        fetchDashboardSummary()
    }

    private fun fetchDashboardSummary() {
        ApiClient.instance.getDashboardSummary().enqueue(object : Callback<DashboardSummary> {
            override fun onResponse(call: Call<DashboardSummary>, response: Response<DashboardSummary>) {
                if (response.isSuccessful) {
                    response.body()?.let { summary ->
                        binding.appliancesCount.text = summary.totalAppliances.toString()
                        binding.techniciansCount.text = summary.totalTechnicians.toString()
                        binding.pendingIssuesCount.text = summary.pendingIssues.toString()
                        binding.warrantyExpiryCount.text = summary.warrantyExpiry.toString()

                        // This needs to be updated to fetch assigned tasks
                        // binding.assignedTasksRecyclerView.adapter = SupervisorAssignedTasksAdapter(summary.assignedTasks)
                    }
                } else {
                    Toast.makeText(this@SupervisorDashboardActivity, "Failed to load summary", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DashboardSummary>, t: Throwable) {
                Toast.makeText(this@SupervisorDashboardActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
