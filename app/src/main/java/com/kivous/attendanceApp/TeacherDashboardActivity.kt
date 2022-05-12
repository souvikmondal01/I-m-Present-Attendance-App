package com.kivous.attendanceApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TeacherDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dash_board)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)

        val ivClose: ImageView = findViewById(R.id.iv_back)
        val vClose: View = findViewById(R.id.v_back)
        val takeAttendance: ImageView = findViewById(R.id.iv_take_attendance)
        val ivAccount: ImageView = findViewById(R.id.iv_account)
        val vAccount: View = findViewById(R.id.v_account)

        ivClose.setOnClickListener {
            finish()
        }
        vClose.setOnClickListener {
            finish()
        }
        takeAttendance.setOnClickListener {
            startActivity(Intent(this, TeacherTakeAttendanceActivity::class.java))
        }
        ivAccount.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        vAccount.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }


}