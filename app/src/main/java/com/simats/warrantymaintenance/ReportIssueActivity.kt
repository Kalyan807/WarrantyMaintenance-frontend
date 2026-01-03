package com.simats.warrantymaintenance

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.ReportIssueResponse
import com.simats.warrantymaintenance.databinding.ActivityReportIssueBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.simats.warrantymaintenance.utils.SessionManager
import java.io.File
import java.io.FileOutputStream

class ReportIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportIssueBinding
    private var imageUri: Uri? = null

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUri = it.data?.data
            binding.imagePreview.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.choosePhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            filePickerLauncher.launch(intent)
        }

        binding.submitIssueButton.setOnClickListener {
            submitIssue()
        }
    }

    private fun submitIssue() {
        val applianceName = binding.applianceNameInput.text.toString().trim()
        val issueDescription = binding.issueDescriptionInput.text.toString().trim()

        if (applianceName.isEmpty() || issueDescription.isEmpty()) {
            Toast.makeText(this, "Appliance Name and Description are required", Toast.LENGTH_SHORT).show()
            return
        }

        val appliancePart = applianceName.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = issueDescription.toRequestBody("text/plain".toMediaTypeOrNull())
        
        // Get logged-in user ID
        val userId = SessionManager.getUserId(this)
        val userIdPart = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        var imagePart: MultipartBody.Part? = null
        imageUri?.let {
            val imageFile = getFileFromUri(it)
            if (imageFile != null) {
                imagePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }
        }

        ApiClient.instance.reportIssue(appliancePart, descriptionPart, userIdPart, imagePart)
            .enqueue(object : Callback<ReportIssueResponse> {
                override fun onResponse(call: Call<ReportIssueResponse>, response: Response<ReportIssueResponse>) {
                    if (response.isSuccessful) {
                        val reportIssueResponse = response.body()
                        if (reportIssueResponse?.status == "success") {
                            Toast.makeText(this@ReportIssueActivity, "Issue reported successfully", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            val errorMessage = reportIssueResponse?.errors?.joinToString("\n") ?: reportIssueResponse?.message
                            Toast.makeText(this@ReportIssueActivity, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = "Error: ${response.code()} ${response.message()}\n$errorBody"
                        Toast.makeText(this@ReportIssueActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ReportIssueResponse>, t: Throwable) {
                    Toast.makeText(this@ReportIssueActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
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
