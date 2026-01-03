package com.simats.warrantymaintenance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.warrantymaintenance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.supervisorCard.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("ROLE", "SUPERVISOR")
            startActivity(intent)
        }

        binding.technicianCard.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("ROLE", "TECHNICIAN")
            startActivity(intent)
        }

        binding.userCard.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("ROLE", "USER")
            startActivity(intent)
        }
    }
}
