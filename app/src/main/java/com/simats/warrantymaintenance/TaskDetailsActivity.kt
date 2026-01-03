package com.simats.warrantymaintenance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.TaskDetails
import com.simats.warrantymaintenance.databinding.ActivityTaskDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val taskId = intent.getIntExtra("TASK_ID", -1)
        if (taskId != -1) {
            fetchTaskDetails(taskId)
        }

        binding.callCustomerButton.setOnClickListener {
            // Implement call functionality
        }

        binding.markCompletedButton.setOnClickListener {
            // Implement mark completed functionality
        }
    }

    private fun fetchTaskDetails(taskId: Int) {
        ApiClient.instance.getTaskDetails(taskId).enqueue(object : Callback<TaskDetails> {
            override fun onResponse(call: Call<TaskDetails>, response: Response<TaskDetails>) {
                if (response.isSuccessful) {
                    response.body()?.let { details ->
                        binding.applianceName.text = details.applianceName
                        binding.issueDescription.text = details.issueDescription
                        binding.priorityText.text = details.priority
                        binding.customerNameText.text = details.customerName
                        binding.customerPhoneText.text = details.customerPhone
                        binding.supervisorNotesText.text = details.supervisorNotes
                    }
                } else {
                    Toast.makeText(this@TaskDetailsActivity, "Failed to load task details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TaskDetails>, t: Throwable) {
                Toast.makeText(this@TaskDetailsActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
