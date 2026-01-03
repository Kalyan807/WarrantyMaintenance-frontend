package com.simats.warrantymaintenance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.api.ApiClient
import com.simats.warrantymaintenance.data.LoginRequest
import com.simats.warrantymaintenance.data.LoginResponse
import com.simats.warrantymaintenance.databinding.ActivityLoginBinding
import com.simats.warrantymaintenance.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val role = intent.getStringExtra("ROLE")

        binding.signInButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(email, password)
            ApiClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.status == "success") {
                            // Save user session
                            loginResponse.user?.let { user ->
                                SessionManager.saveUserSession(
                                    this@LoginActivity,
                                    user.id,
                                    user.full_name,
                                    user.email,
                                    role
                                )
                            }
                            
                            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                            val intent = when (role) {
                                "SUPERVISOR" -> Intent(this@LoginActivity, SupervisorDashboardActivity::class.java)
                                "TECHNICIAN" -> Intent(this@LoginActivity, TechnicianDashboardActivity::class.java)
                                "USER" -> Intent(this@LoginActivity, UserDashboardActivity::class.java)
                                else -> Intent(this@LoginActivity, MainActivity::class.java)
                            }
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, loginResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.signUpText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.putExtra("ROLE", role)
            startActivity(intent)
        }

        binding.signUpLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.putExtra("ROLE", role)
            startActivity(intent)
        }
    }
}