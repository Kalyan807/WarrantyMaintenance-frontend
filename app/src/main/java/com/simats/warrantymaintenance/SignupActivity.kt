package com.simats.warrantymaintenance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.SignupRequest
import com.simats.warrantymaintenance.data.SignupResponse
import com.simats.warrantymaintenance.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val role = intent.getStringExtra("ROLE")

        binding.createAccountButton.setOnClickListener {
            val fullName = binding.fullNameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val phone = binding.phoneInput.text.toString().trim()
            val address = binding.addressInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            val confirmPassword = binding.confirmPasswordInput.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val signupRequest = SignupRequest(fullName, email, phone, address, password, confirmPassword, role ?: "USER")

            ApiClient.instance.signup(signupRequest).enqueue(object : Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    if (response.isSuccessful) {
                        val signupResponse = response.body()
                        if (signupResponse?.status == "success") {
                            Toast.makeText(this@SignupActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@SignupActivity, signupResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = "Error: ${response.code()} ${response.message()}\n$errorBody"
                        Toast.makeText(this@SignupActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.signInText.setOnClickListener {
            finish()
        }
    }
}
