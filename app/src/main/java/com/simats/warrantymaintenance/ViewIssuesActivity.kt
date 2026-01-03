package com.simats.warrantymaintenance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.warrantymaintenance.adapter.IssuesAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.IssuesResponse
import com.simats.warrantymaintenance.databinding.ActivityViewIssuesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewIssuesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewIssuesBinding

    private val assignTechnicianLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            fetchIssues()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewIssuesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.issuesRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchIssues()
    }

    private fun fetchIssues() {
        ApiClient.instance.getIssues().enqueue(object : Callback<IssuesResponse> {
            override fun onResponse(call: Call<IssuesResponse>, response: Response<IssuesResponse>) {
                if (response.isSuccessful) {
                    val issues = response.body()?.issues ?: emptyList()
                    binding.issuesRecyclerView.adapter = IssuesAdapter(issues) { issue ->
                        val intent = Intent(this@ViewIssuesActivity, AssignTechnicianActivity::class.java).apply {
                            putExtra("ISSUE_ID", issue.id)
                            putExtra("APPLIANCE_NAME", issue.applianceName)
                            putExtra("PROBLEM_DESCRIPTION", issue.issueDescription)
                            putExtra("LOCATION", "123 Main St, Apt 4B") // Placeholder
                            putExtra("REPORTED_BY", issue.reportedBy)
                        }
                        assignTechnicianLauncher.launch(intent)
                    }
                } else {
                    Toast.makeText(this@ViewIssuesActivity, "Failed to load issues", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<IssuesResponse>, t: Throwable) {
                Toast.makeText(this@ViewIssuesActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
