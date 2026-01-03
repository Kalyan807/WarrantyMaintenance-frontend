package com.simats.warrantymaintenance

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.AddWarrantyResponse
import com.simats.warrantymaintenance.databinding.ActivityAddWarrantyBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class AddWarrantyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddWarrantyBinding
    private var documentUri: Uri? = null

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            documentUri = it.data?.data
            binding.uploadDocumentButton.text = getFileName(documentUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWarrantyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.purchaseDateInput.setOnClickListener { showDatePickerDialog(true) }
        binding.warrantyExpiryDateInput.setOnClickListener { showDatePickerDialog(false) }

        binding.uploadDocumentButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            filePickerLauncher.launch(intent)
        }

        binding.saveWarrantyButton.setOnClickListener {
            saveWarranty()
        }
    }

    private fun showDatePickerDialog(isPurchaseDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, {
            _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            if (isPurchaseDate) {
                binding.purchaseDateInput.setText(date)
            } else {
                binding.warrantyExpiryDateInput.setText(date)
            }
        }, year, month, day).show()
    }

    private fun saveWarranty() {
        val applianceName = binding.applianceNameInput.text.toString().trim()
        val modelNumber = binding.modelNumberInput.text.toString().trim()
        val purchaseDate = binding.purchaseDateInput.text.toString().trim()
        val expiryDate = binding.warrantyExpiryDateInput.text.toString().trim()
        val maintenanceFrequency = binding.maintenanceFrequencyInput.text.toString().trim()
        val notes = binding.notesInput.text.toString().trim()

        if (applianceName.isEmpty() || modelNumber.isEmpty() || purchaseDate.isEmpty() || expiryDate.isEmpty() || maintenanceFrequency.isEmpty() || documentUri == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val documentFile = getFileFromUri(documentUri!!)
        if (documentFile == null) {
            Toast.makeText(this, "Failed to get file from URI", Toast.LENGTH_SHORT).show()
            return
        }

        val appliancePart = applianceName.toRequestBody("text/plain".toMediaTypeOrNull())
        val modelNumberPart = modelNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val purchaseDatePart = purchaseDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val expiryDatePart = expiryDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val maintenanceFrequencyPart = maintenanceFrequency.toRequestBody("text/plain".toMediaTypeOrNull())
        val notesPart = notes.toRequestBody("text/plain".toMediaTypeOrNull())
        val documentPart = MultipartBody.Part.createFormData(
            "document",
            documentFile.name,
            documentFile.asRequestBody("image/*".toMediaTypeOrNull())
        )

        ApiClient.instance.addWarranty(appliancePart, modelNumberPart, purchaseDatePart, expiryDatePart, maintenanceFrequencyPart, notesPart, documentPart)
            .enqueue(object : Callback<AddWarrantyResponse> {
                override fun onResponse(call: Call<AddWarrantyResponse>, response: Response<AddWarrantyResponse>) {
                    if (response.isSuccessful) {
                        val addWarrantyResponse = response.body()
                        if (addWarrantyResponse?.status == "success") {
                            Toast.makeText(this@AddWarrantyActivity, "Warranty record added successfully", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            val errorMessage = addWarrantyResponse?.errors?.joinToString("\n") ?: addWarrantyResponse?.message
                            Toast.makeText(this@AddWarrantyActivity, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@AddWarrantyActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddWarrantyResponse>, t: Throwable) {
                    Toast.makeText(this@AddWarrantyActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getFileName(uri: Uri?): String {
        var fileName = ""
        uri?.let {
            contentResolver.query(it, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        fileName = cursor.getString(displayNameIndex)
                    }
                }
            }
        }
        return fileName
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, getFileName(uri))
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
