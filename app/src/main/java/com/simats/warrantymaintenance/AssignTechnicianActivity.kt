package com.simats.warrantymaintenance

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.AssignTaskRequest
import com.simats.warrantymaintenance.data.AssignTaskResponse
import com.simats.warrantymaintenance.data.Technician
import com.simats.warrantymaintenance.data.TechniciansResponse
import com.simats.warrantymaintenance.adapter.TechnicianSpinnerAdapter
import com.simats.warrantymaintenance.databinding.ActivityAssignTechnicianBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AssignTechnicianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssignTechnicianBinding
    private var selectedTechnicianId: Int = -1
    private val techniciansMap = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignTechnicianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val issueId = intent.getIntExtra("ISSUE_ID", -1)
        val applianceName = intent.getStringExtra("APPLIANCE_NAME")
        val problemDescription = intent.getStringExtra("PROBLEM_DESCRIPTION")
        val location = intent.getStringExtra("LOCATION")
        val reportedBy = intent.getStringExtra("REPORTED_BY")

        binding.applianceNameText.text = applianceName
        binding.problemDescriptionText.text = problemDescription
        binding.locationText.text = location
        binding.reportedByText.text = reportedBy

        fetchTechnicians()

        binding.scheduleDateInput.setOnClickListener {
            showDateTimePicker()
        }

        binding.assignTaskButton.setOnClickListener {
            val scheduleDateTime = binding.scheduleDateInput.text.toString()
            val notes = binding.notesInput.text.toString().trim()

            if (selectedTechnicianId == -1 || scheduleDateTime.isEmpty()) {
                Toast.makeText(this, "Please select a technician and schedule a date and time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = AssignTaskRequest(issueId, selectedTechnicianId, scheduleDateTime, notes)
            assignTask(request)
        }
    }

    private fun fetchTechnicians() {
        ApiClient.instance.getTechnicians().enqueue(object : Callback<TechniciansResponse> {
            override fun onResponse(call: Call<TechniciansResponse>, response: Response<TechniciansResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        if (data.technicians.isEmpty()) {
                            Toast.makeText(this@AssignTechnicianActivity, "No technicians available", Toast.LENGTH_SHORT).show()
                            return
                        }
                        
                        // Create list with placeholder (null) at position 0 followed by actual technicians
                        val techniciansList = mutableListOf<Technician?>(null)
                        techniciansList.addAll(data.technicians)
                        
                        techniciansMap.clear()
                        data.technicians.forEach { techniciansMap[it.name] = it.id }

                        val adapter = TechnicianSpinnerAdapter(this@AssignTechnicianActivity, techniciansList)
                        binding.technicianSpinner.adapter = adapter

                        // Add spinner selection listener
                        binding.technicianSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position == 0) {
                                    // Placeholder selected
                                    selectedTechnicianId = -1
                                } else {
                                    val selectedTechnician = techniciansList[position]
                                    selectedTechnicianId = selectedTechnician?.id ?: -1
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                selectedTechnicianId = -1
                            }
                        }
                    } ?: run {
                        Toast.makeText(this@AssignTechnicianActivity, "Empty response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@AssignTechnicianActivity, "Failed to load technicians: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TechniciansResponse>, t: Throwable) {
                Toast.makeText(this@AssignTechnicianActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, {
            _, year, month, dayOfMonth ->
            TimePickerDialog(this, {
                _, hourOfDay, minute ->
                val scheduleDateTime = "$year-${month + 1}-$dayOfMonth $hourOfDay:$minute:00"
                binding.scheduleDateInput.setText(scheduleDateTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun assignTask(request: AssignTaskRequest) {
        ApiClient.instance.assignTask(request).enqueue(object : Callback<AssignTaskResponse> {
            override fun onResponse(call: Call<AssignTaskResponse>, response: Response<AssignTaskResponse>) {
                if (response.isSuccessful) {
                    val assignTaskResponse = response.body()
                    if (assignTaskResponse?.status == "success") {
                        Toast.makeText(this@AssignTechnicianActivity, "Task assigned successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(this@AssignTechnicianActivity, assignTaskResponse?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AssignTechnicianActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AssignTaskResponse>, t: Throwable) {
                Toast.makeText(this@AssignTechnicianActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
