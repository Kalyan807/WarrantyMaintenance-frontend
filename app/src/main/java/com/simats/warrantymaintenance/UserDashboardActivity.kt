package com.simats.warrantymaintenance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.simats.warrantymaintenance.adapter.AppliancesAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.AppliancesResponse
import com.simats.warrantymaintenance.databinding.ActivityUserDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDashboardBinding

    private val reportIssueLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            fetchAppliances()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.appliancesRecyclerView.layoutManager = GridLayoutManager(this, 2)

        binding.fabAddAppliance.setOnClickListener {
            val intent = Intent(this, ReportIssueActivity::class.java)
            reportIssueLauncher.launch(intent)
        }

        // Setup bottom navigation
        binding.bottomNavigation.selectedItemId = R.id.appliances
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.appliances -> true
                R.id.services -> {
                    startActivity(Intent(this, ServiceTrackingActivity::class.java))
                    true
                }
                R.id.alerts -> {
                    // Navigate to alerts/notifications
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    true
                }
                R.id.profile -> {
                    // Navigate to profile/settings
                    true
                }
                else -> false
            }
        }

        fetchAppliances()
    }

    private fun fetchAppliances() {
        ApiClient.instance.getUserAppliances().enqueue(object : Callback<AppliancesResponse> {
            override fun onResponse(call: Call<AppliancesResponse>, response: Response<AppliancesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        binding.registeredAppliancesText.text = "${data.registeredAppliances} registered appliances"
                        binding.appliancesRecyclerView.adapter = AppliancesAdapter(data.appliances)
                    }
                } else {
                    Toast.makeText(this@UserDashboardActivity, "Failed to load appliances", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AppliancesResponse>, t: Throwable) {
                Toast.makeText(this@UserDashboardActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
