package com.kivous.attendanceApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

lateinit var auth: FirebaseAuth

@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_800)
        maintainSession()
    }

    //    Function for maintain session (if already logged in then directly open dashBoard activity)
    private fun maintainSession() {
        val token = getSharedPreferences("username", Context.MODE_PRIVATE)
        if (token.getString("login_email", " ") != " ") {

            db.collection("users").document(token.getString("login_email", "").toString()).get()
                .addOnSuccessListener { t ->
                    val c = t.getString("student")
                    if (c == "0") {
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this, TeacherDashboardActivity::class.java))
                            finish()
                        }, 500)
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this, StudentDashboardActivity::class.java))
                            finish()
                        }, 500)
                    }
                }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }, 500)
        }
    }

}