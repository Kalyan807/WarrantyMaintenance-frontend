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
import com.simats.warrantymaintenance.data.AddTechnicianResponse
import com.simats.warrantymaintenance.databinding.ActivityAddTechnicianBinding
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

class AddTechnicianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTechnicianBinding
    private var idProofUri: Uri? = null

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            idProofUri = it.data?.data
            binding.idProofButton.text = getFileName(idProofUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTechnicianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.idProofButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            filePickerLauncher.launch(intent)
        }

        binding.saveTechnicianButton.setOnClickListener {
            saveTechnician()
        }
    }

    private fun saveTechnician() {
        val name = binding.fullNameInput.text.toString().trim()
        val phone = binding.phoneNumberInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val experience = binding.experienceInput.text.toString().trim()
        val specialization = binding.specializationInput.text.toString().trim()
        val address = binding.addressInput.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || experience.isEmpty() || specialization.isEmpty() || address.isEmpty() || idProofUri == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val idProofFile = getFileFromUri(idProofUri!!)
        if (idProofFile == null) {
            Toast.makeText(this, "Failed to get file from URI", Toast.LENGTH_SHORT).show()
            return
        }

        val namePart: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val phonePart: RequestBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailPart: RequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val experiencePart: RequestBody = experience.toRequestBody("text/plain".toMediaTypeOrNull())
        val specializationPart: RequestBody = specialization.toRequestBody("text/plain".toMediaTypeOrNull())
        val addressPart: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())

        val idProofPart = MultipartBody.Part.createFormData(
            "id_proof",
            idProofFile.name,
            idProofFile.asRequestBody("image/*".toMediaTypeOrNull())
        )

        ApiClient.instance.addTechnician(namePart, phonePart, emailPart, experiencePart, specializationPart, addressPart, idProofPart)
            .enqueue(object : Callback<AddTechnicianResponse> {
                override fun onResponse(call: Call<AddTechnicianResponse>, response: Response<AddTechnicianResponse>) {
                    if (response.isSuccessful) {
                        val addTechnicianResponse = response.body()
                        if (addTechnicianResponse?.status == "success") {
                            Toast.makeText(this@AddTechnicianActivity, "Technician added successfully", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            val errorMessage = addTechnicianResponse?.errors?.joinToString("\n") ?: addTechnicianResponse?.message
                            Toast.makeText(this@AddTechnicianActivity, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@AddTechnicianActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddTechnicianResponse>, t: Throwable) {
                    Toast.makeText(this@AddTechnicianActivity, t.message, Toast.LENGTH_SHORT).show()
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
