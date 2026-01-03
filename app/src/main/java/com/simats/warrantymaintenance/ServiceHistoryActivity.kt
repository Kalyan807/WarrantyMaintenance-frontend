package com.simats.warrantymaintenance

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.warrantymaintenance.adapter.ServiceHistoryAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.ServiceHistoryResponse
import com.simats.warrantymaintenance.databinding.ActivityServiceHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.serviceHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchServiceHistory()
    }

    private fun fetchServiceHistory() {
        ApiClient.instance.getServiceHistory().enqueue(object : Callback<ServiceHistoryResponse> {
            override fun onResponse(call: Call<ServiceHistoryResponse>, response: Response<ServiceHistoryResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        binding.totalServicesCount.text = data.totalServices.toString()
                        binding.serviceHistoryRecyclerView.adapter = ServiceHistoryAdapter(data.serviceHistory)
                    }
                } else {
                    Toast.makeText(this@ServiceHistoryActivity, "Failed to load service history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ServiceHistoryResponse>, t: Throwable) {
                Toast.makeText(this@ServiceHistoryActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
