package com.simats.warrantymaintenance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.warrantymaintenance.adapter.TechniciansAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.TechniciansResponse
import com.simats.warrantymaintenance.databinding.ActivityTechniciansBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TechniciansActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTechniciansBinding

    private val addTechnicianLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            fetchTechnicians()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechniciansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.techniciansRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.fabAddTechnician.setOnClickListener {
            val intent = Intent(this, AddTechnicianActivity::class.java)
            addTechnicianLauncher.launch(intent)
        }

        fetchTechnicians()
    }

    private fun fetchTechnicians() {
        ApiClient.instance.getTechnicians().enqueue(object : Callback<TechniciansResponse> {
            override fun onResponse(call: Call<TechniciansResponse>, response: Response<TechniciansResponse>) {
                if (response.isSuccessful) {
                    val technicians = response.body()?.technicians ?: emptyList()
                    binding.techniciansRecyclerView.adapter = TechniciansAdapter(technicians)
                } else {
                    Toast.makeText(this@TechniciansActivity, "Failed to load technicians", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TechniciansResponse>, t: Throwable) {
                Toast.makeText(this@TechniciansActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
