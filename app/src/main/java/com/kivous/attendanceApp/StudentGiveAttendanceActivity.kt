package com.kivous.attendanceApp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.text.SimpleDateFormat
import java.util.*

class StudentGiveAttendanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_give_attendance)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)

        val tfBranch: AutoCompleteTextView = findViewById(R.id.tf_branch)
        val tfSubject: AutoCompleteTextView = findViewById(R.id.tf_subject)
        val etDate: EditText = findViewById(R.id.et_date)
        val ivClose: ImageView = findViewById(R.id.iv_close)
        val btnPost: Button = findViewById(R.id.btn_post)
        val etName: EditText = findViewById(R.id.et_name)
        val etRoll: EditText = findViewById(R.id.et_roll)
        val etCode: EditText = findViewById(R.id.et_code)
        val swipeLayout: SwipeRefreshLayout = findViewById(R.id.swipeLayout)

        val branchOptions = resources.getStringArray(R.array.branch)
        val branchAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, branchOptions)
        tfBranch.setAdapter(branchAdapter)

        val subjectOptions = resources.getStringArray(R.array.subject)
        val subjectAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectOptions)
        tfSubject.setAdapter(subjectAdapter)

        val d: String = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())
        etDate.setText(d)

        ivClose.setOnClickListener {
            finish()
        }

        val currentUser = auth.currentUser!!.uid
        val codeCollection = db.collection("code")
        codeCollection.document("code").get().addOnSuccessListener { tasks ->
            val teacherCode = tasks.get("code").toString()

            btnPost.setOnClickListener {
                val name = etName.text.toString().trim()
                val roll = etRoll.text.toString().trim()
                val code = etCode.text.toString().trim()
                val date = etDate.text.toString().trim()
                val branch = tfBranch.editableText.toString().trim()
                val subject = tfSubject.editableText.toString().trim()
                val currentTime = System.currentTimeMillis()

                if (etDate.text.isEmpty()) {
                    etDate.error = "select date"
                } else if (etName.text.isEmpty()) {
                    etName.error = "enter name"
                } else if (etRoll.text.isEmpty()) {
                    etRoll.error = "enter roll"
                } else if (tfBranch.text.isEmpty()) {
                    tfBranch.error = "select branch"
                } else if (tfSubject.text.isEmpty()) {
                    tfSubject.error = "select subject"
                } else {
                    if (code == teacherCode) {
                        val info = hashMapOf(
                            "name" to name,
                            "roll" to roll,
                            "code" to code,
                            "date" to date,
                            "branch" to branch,
                            "subject" to subject,
                            "time" to currentTime
                        )
                        val cl = "$date$branch$subject"
                        val attendanceInfo = db.collection(cl)
                        attendanceInfo.document(currentUser).set(info)

                        db.collection(currentUser).document().set(info)
                            .addOnSuccessListener {
                                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
                            }
                        startActivity(Intent(this, StudentCheckAttendanceActivity::class.java))
                        finish()
                    } else {
                        etCode.error = "wrong code"
                    }
                }
            }
        }

        db.collection("users").document(currentUser).get().addOnSuccessListener { tasks ->
            etName.setText(tasks.get("name").toString())
            etRoll.setText(tasks.get("classroll").toString())
            tfBranch.setText(tasks.get("branch2").toString())
        }
        swipeLayout.setOnRefreshListener {
            db.collection("users").document(currentUser).get().addOnSuccessListener { tasks ->
                etName.setText(tasks.get("name").toString())
                etRoll.setText(tasks.get("classroll").toString())
                tfBranch.setText(tasks.get("branch2").toString())
            }
            swipeLayout.isRefreshing = false
        }


    }

}