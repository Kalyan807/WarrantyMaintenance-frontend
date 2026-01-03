package com.simats.warrantymaintenance

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.warrantymaintenance.adapter.WarrantyExpiryAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.WarrantyExpiryResponse
import com.simats.warrantymaintenance.databinding.ActivityWarrantyExpiryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WarrantyExpiryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWarrantyExpiryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarrantyExpiryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.warrantyExpiryRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchWarrantyExpiryData()
    }

    private fun fetchWarrantyExpiryData() {
        ApiClient.instance.getWarrantyExpiry().enqueue(object : Callback<WarrantyExpiryResponse> {
            override fun onResponse(call: Call<WarrantyExpiryResponse>, response: Response<WarrantyExpiryResponse>) {
                if (response.isSuccessful) {
                    val warranties = response.body()?.warranties ?: emptyList()
                    binding.warrantyExpiryRecyclerView.adapter = WarrantyExpiryAdapter(warranties)
                } else {
                    Toast.makeText(this@WarrantyExpiryActivity, "Failed to load warranty expiry data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WarrantyExpiryResponse>, t: Throwable) {
                Toast.makeText(this@WarrantyExpiryActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
