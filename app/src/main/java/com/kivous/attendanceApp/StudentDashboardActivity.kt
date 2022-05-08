package com.kivous.attendanceApp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class StudentDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)
        auth = FirebaseAuth.getInstance()

        val ivAccount: ImageView = findViewById(R.id.iv_account)
        val ivGiveAttendance: ImageView = findViewById(R.id.iv_give_attendance)
        val ivCheckAttendance: ImageView = findViewById(R.id.iv_check_attendance)
        val back: ImageView = findViewById(R.id.iv_back)

        back.setOnClickListener {
            finish()
        }
        ivAccount.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        ivGiveAttendance.setOnClickListener {
            startActivity(Intent(this, StudentGiveAttendanceActivity::class.java))
        }
        ivCheckAttendance.setOnClickListener {
            startActivity(Intent(this, StudentCheckAttendanceActivity::class.java))
        }
    }

}
