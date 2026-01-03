package com.simats.warrantymaintenance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.ServiceTracking
import com.simats.warrantymaintenance.data.ServiceTrackingResponse
import com.simats.warrantymaintenance.databinding.ActivityServiceTrackingBinding
import com.simats.warrantymaintenance.databinding.ItemProgressStepBinding
import com.simats.warrantymaintenance.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceTrackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Setup bottom navigation
        binding.bottomNavigation.selectedItemId = R.id.services
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.appliances -> {
                    startActivity(Intent(this, UserDashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.services -> true
                R.id.alerts -> {
                    // Navigate to alerts if available
                    true
                }
                R.id.profile -> {
                    // Navigate to profile if available
                    true
                }
                else -> false
            }
        }

        fetchServiceTracking()
    }

    private fun fetchServiceTracking() {
        val userId = SessionManager.getUserId(this)
        ApiClient.instance.getServiceTracking(userId).enqueue(object : Callback<ServiceTrackingResponse> {
            override fun onResponse(call: Call<ServiceTrackingResponse>, response: Response<ServiceTrackingResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        if (data.status == "success" && data.service != null) {
                            displayServiceTracking(data.service)
                        } else {
                            showNoServiceMessage()
                        }
                    } ?: showNoServiceMessage()
                } else {
                    Toast.makeText(this@ServiceTrackingActivity, "Failed to load service tracking", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ServiceTrackingResponse>, t: Throwable) {
                Toast.makeText(this@ServiceTrackingActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayServiceTracking(service: ServiceTracking) {
        // Service Request details
        binding.applianceName.text = service.appliance
        binding.issueDescription.text = service.issue
        binding.statusChip.text = service.status

        // Progress timeline
        setupProgressStep(
            binding.stepReported,
            "Issue Reported",
            service.reportedDate,
            true,
            service.assignedDate != null
        )

        setupProgressStep(
            binding.stepAssigned,
            "Technician Assigned",
            service.assignedDate ?: "-",
            service.assignedDate != null,
            service.inProgressDate != null
        )

        setupProgressStep(
            binding.stepInProgress,
            "In Progress",
            service.inProgressDate ?: "-",
            service.inProgressDate != null,
            service.completedDate != null
        )

        setupProgressStep(
            binding.stepCompleted,
            "Completed",
            service.completedDate ?: "-",
            service.completedDate != null,
            false
        )

        // Assigned Technician
        service.technician?.let { tech ->
            binding.technicianCard.visibility = View.VISIBLE
            binding.technicianName.text = tech.name
            binding.expectedVisit.text = tech.expectedVisit ?: "To be scheduled"

            binding.callTechnicianButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${tech.phone}")
                startActivity(intent)
            }
        }
    }

    private fun setupProgressStep(
        stepBinding: ItemProgressStepBinding,
        title: String,
        date: String,
        isCompleted: Boolean,
        showLine: Boolean
    ) {
        stepBinding.stepTitle.text = title
        stepBinding.stepDate.text = date

        if (isCompleted) {
            stepBinding.stepIcon.setImageResource(R.drawable.ic_check_circle)
            stepBinding.stepIcon.setColorFilter(ContextCompat.getColor(this, R.color.success))
            stepBinding.stepTitle.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        } else {
            stepBinding.stepIcon.setImageResource(R.drawable.ic_check_circle)
            stepBinding.stepIcon.setColorFilter(ContextCompat.getColor(this, R.color.divider))
            stepBinding.stepTitle.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
        }

        stepBinding.stepLine.visibility = if (showLine) View.VISIBLE else View.GONE
    }

    private fun showNoServiceMessage() {
        Toast.makeText(this, "No active service requests", Toast.LENGTH_SHORT).show()
    }
}
