package com.simats.warrantymaintenance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.TechnicianProfile
import com.simats.warrantymaintenance.databinding.ActivityTechnicianProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TechnicianProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTechnicianProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.changePasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        fetchTechnicianProfile()
    }

    private fun fetchTechnicianProfile() {
        ApiClient.instance.getTechnicianProfile().enqueue(object : Callback<TechnicianProfile> {
            override fun onResponse(call: Call<TechnicianProfile>, response: Response<TechnicianProfile>) {
                if (response.isSuccessful) {
                    response.body()?.let { profile ->
                        binding.technicianName.text = profile.name
                        binding.technicianSpecialization.text = profile.specialization
                        binding.technicianRating.text = "${profile.rating} Rating"
                        binding.completedCount.text = profile.completedTasks.toString()
                        binding.experienceYears.text = profile.experience.toString()
                        binding.successRate.text = "${profile.successRate}%"
                        binding.specializationValue.text = profile.specialization
                        binding.experienceValue.text = "${profile.experience} Years"
                    }
                } else {
                    Toast.makeText(this@TechnicianProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TechnicianProfile>, t: Throwable) {
                Toast.makeText(this@TechnicianProfileActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
