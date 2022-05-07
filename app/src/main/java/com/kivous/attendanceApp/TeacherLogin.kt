package com.kivous.attendanceApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kivous.attendanceApp.databinding.ActivityTeacherLoginBinding

class TeacherLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityTeacherLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.teal_200)

        binding.tvBackArrow.setOnClickListener {
            finish()
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show()
        }
        binding.tvRegister.setOnClickListener {
            Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show()
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,TeacherDashBoard::class.java))
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
        }

    }
}