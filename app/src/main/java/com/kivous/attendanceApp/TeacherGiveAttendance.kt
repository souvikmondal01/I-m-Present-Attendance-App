package com.kivous.attendanceApp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class TeacherGiveAttendance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_give_attendance)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)

        val tfBranch: AutoCompleteTextView = findViewById(R.id.tf_branch)
        val tfSubject: AutoCompleteTextView = findViewById(R.id.tf_subject)
        val etDate: EditText = findViewById(R.id.et_date)
        val ivClose: ImageView = findViewById(R.id.iv_close)
        val btnPost: Button = findViewById(R.id.btn_post)
        val btnList: Button = findViewById(R.id.btn_see_attendance_list)
        val etName: EditText = findViewById(R.id.et_name)
        val etRoll: EditText = findViewById(R.id.et_roll)

        ivClose.setOnClickListener {
            finish()
        }
        val d: String = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())
        etDate.setText(d)

        val branchOptions = arrayOf("CSE-AIML", "CSE-DS", "CSE-CS")
        val branchAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, branchOptions)
        tfBranch.setAdapter(branchAdapter)

        val subjectOptions = arrayOf("Java", "Java Lab", "DAA", "DAA Lab")
        val subjectAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectOptions)
        tfSubject.setAdapter(subjectAdapter)

        btnPost.setOnClickListener {
            val name = etName.text.toString().trim()
            val roll = etRoll.text.toString().trim()
            val date = etDate.text.toString().trim()
            val branch = tfBranch.editableText.toString().trim()
            val subject = tfSubject.editableText.toString().trim()

            val info = hashMapOf(
                "name" to name,
                "roll" to roll,
                "date" to date,
                "branch" to branch,
                "subject" to subject
            )
            val cl = "$date$branch$subject"
            val attendanceInfo = db.collection(cl)
            attendanceInfo.document().set(info)
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
        }

        btnList.setOnClickListener {
            when {
                etDate.text.isEmpty() -> {
                    etDate.error = "select date"
                }
                tfBranch.editableText.isEmpty() -> {
                    tfBranch.error = "select branch"
                }
                tfSubject.editableText.isEmpty() -> {
                    tfSubject.error = "select subject"
                }
                else -> {
                    val date = etDate.text.toString().trim()
                    val branch = tfBranch.editableText.toString().trim()
                    val subject = tfSubject.editableText.toString().trim()
                    val collection = "$date$branch$subject"
                    val intent = Intent(this, AttendanceList::class.java)
                    intent.putExtra("collection", collection)
                    startActivity(intent)
                }
            }
        }


    }

}
