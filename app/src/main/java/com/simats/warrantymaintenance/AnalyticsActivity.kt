package com.simats.warrantymaintenance

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.simats.warrantymaintenance.adapter.TechnicianPerformanceAdapter
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.AnalyticsData
import com.simats.warrantymaintenance.databinding.ActivityAnalyticsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.technicianPerformanceRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchAnalyticsData()
    }

    private fun fetchAnalyticsData() {
        ApiClient.instance.getAnalyticsData().enqueue(object : Callback<AnalyticsData> {
            override fun onResponse(call: Call<AnalyticsData>, response: Response<AnalyticsData>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        setupSummary(data)
                        setupIssuesByStatus(data.issuesByStatus)
                        setupTechnicianPerformance(data.technicianPerformance)
                        setupMonthlyServiceChart(data.monthlyServiceCount)
                    }
                } else {
                    Toast.makeText(this@AnalyticsActivity, "Failed to load analytics data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AnalyticsData>, t: Throwable) {
                Toast.makeText(this@AnalyticsActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSummary(data: AnalyticsData) {
        binding.totalIssuesText.text = data.totalIssues.toString()
        binding.issuesChangeText.text = "↑ ${data.issuesChange}% from last month"
        binding.resolvedIssuesText.text = data.resolvedIssues.toString()
        binding.resolutionRateText.text = "${data.resolutionRate}% resolution rate"
    }

    private fun setupIssuesByStatus(issuesByStatus: com.simats.warrantymaintenance.data.IssuesByStatus) {
        // This is a placeholder. You can implement a custom view or use a library to show the progress bars.
    }

    private fun setupTechnicianPerformance(technicians: List<com.simats.warrantymaintenance.data.TechnicianPerformance>) {
        binding.technicianPerformanceRecyclerView.adapter = TechnicianPerformanceAdapter(technicians)
    }

    private fun setupMonthlyServiceChart(monthlyServiceCount: List<Int>) {
        val entries = ArrayList<BarEntry>()
        for ((index, value) in monthlyServiceCount.withIndex()) {
            entries.add(BarEntry(index.toFloat(), value.toFloat()))
        }

        val dataSet = BarDataSet(entries, "Monthly Service Count")
        dataSet.color = Color.rgb(25, 118, 210)

        val barData = BarData(dataSet)
        binding.monthlyServiceChart.data = barData

        val months = arrayOf("Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        binding.monthlyServiceChart.xAxis.valueFormatter = IndexAxisValueFormatter(months)
        binding.monthlyServiceChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.monthlyServiceChart.xAxis.setDrawGridLines(false)

        binding.monthlyServiceChart.axisLeft.setDrawGridLines(false)
        binding.monthlyServiceChart.axisRight.isEnabled = false
        binding.monthlyServiceChart.description.isEnabled = false
        binding.monthlyServiceChart.legend.isEnabled = false
        binding.monthlyServiceChart.invalidate()
    }
}
