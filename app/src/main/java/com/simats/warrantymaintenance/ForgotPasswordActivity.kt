package com.simats.warrantymaintenance

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.ForgotPasswordRequest
import com.simats.warrantymaintenance.data.ForgotPasswordResponse
import com.simats.warrantymaintenance.databinding.ActivityForgotPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.sendResetLinkButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ForgotPasswordRequest(email)
            ApiClient.instance.forgotPassword(request).enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                    if (response.isSuccessful) {
                        val forgotPasswordResponse = response.body()
                        Toast.makeText(this@ForgotPasswordActivity, forgotPasswordResponse?.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ForgotPasswordActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    Toast.makeText(this@ForgotPasswordActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.backToSignInText.setOnClickListener {
            finish()
        }
    }
}