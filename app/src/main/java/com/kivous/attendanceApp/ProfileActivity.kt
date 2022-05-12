package com.kivous.attendanceApp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)

        val back: ImageView = findViewById(R.id.iv_back)
        val vBack: View = findViewById(R.id.v_back)
        val logOut: TextView = findViewById(R.id.tv_logout)
        val vLogOut: View = findViewById(R.id.v_logout)
        val tfBranch: AutoCompleteTextView = findViewById(R.id.tf_branch_profile)
        val tfGender: AutoCompleteTextView = findViewById(R.id.tf_gender_profile)
        val dob: EditText = findViewById(R.id.et_birth_day)
        val name: EditText = findViewById(R.id.et_name)
        val uniroll: EditText = findViewById(R.id.et_uniroll)
        val roll: EditText = findViewById(R.id.et_roll)
        val email: EditText = findViewById(R.id.et_email)
        val update: TextView = findViewById(R.id.tv_update)
        val vUpdate: View = findViewById(R.id.v_update)
        val pb: ProgressBar = findViewById(R.id.pb_profile)
        val branchHide: AutoCompleteTextView = findViewById(R.id.tf_branch_hide)
        val studentHide: AutoCompleteTextView = findViewById(R.id.tf_student_hide)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid

        dob.setOnClickListener {
            datePicker(dob)
        }
        vUpdate.setOnClickListener {
            pb.visibility = View.VISIBLE
            val name2 = name.text.toString().trim()
            val uniroll2 = uniroll.text.toString().trim()
            val roll2 = roll.text.toString().trim()
            val branch2 = tfBranch.text.toString().trim()
            val branchH = branchHide.text.toString().trim()
            val gender2 = tfGender.text.toString().trim()
            val dob2 = dob.text.toString().trim()
            val email2 = email.text.toString().trim()
            val student = studentHide.text.toString().trim()

            val profileInfo = hashMapOf(
                "name" to name2,
                "roll" to uniroll2,
                "classroll" to roll2,
                "branch2" to branch2,
                "branch" to branchH,
                "gender" to gender2,
                "dob" to dob2,
                "email" to email2,
                "student" to student
            )
            db.collection("users").document(currentUser).set(profileInfo).addOnSuccessListener {
                pb.visibility = View.GONE
            }
        }

        db.collection("users").document(currentUser).get().addOnSuccessListener { tasks ->
            name.setText(tasks.get("name").toString())
            uniroll.setText(tasks.get("roll").toString())
            roll.setText(tasks.get("classroll").toString())
            tfBranch.setText(tasks.get("branch2").toString())
            branchHide.setText(tasks.get("branch").toString())
            tfGender.setText(tasks.get("gender").toString())
            dob.setText(tasks.get("dob").toString())
            email.setText(tasks.get("email").toString())
            studentHide.setText(tasks.get("student").toString())
        }

        val token = getSharedPreferences("username", Context.MODE_PRIVATE)
        logOut.setOnClickListener {
            logOut(token)
        }
        vLogOut.setOnClickListener {
            logOut(token)
        }

        back.setOnClickListener {
            finish()
        }
        vBack.setOnClickListener {
            finish()
        }

        val branchOptions = resources.getStringArray(R.array.branch)
        val branchAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, branchOptions)
        tfBranch.setAdapter(branchAdapter)

        val genderOptions = resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, genderOptions)
        tfGender.setAdapter(genderAdapter)
    }

    private fun logOut(token: SharedPreferences) {
        token.edit().putString("login_email", " ").apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }


    private fun datePicker(dateSet: EditText) {

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            var monthInput = (month + 1).toString()
            var dayInput = dayOfMonth.toString()
            when {
                monthInput.toInt() == 1 -> monthInput = "01"
                monthInput.toInt() == 2 -> monthInput = "02"
                monthInput.toInt() == 3 -> monthInput = "03"
                monthInput.toInt() == 4 -> monthInput = "04"
                monthInput.toInt() == 5 -> monthInput = "05"
                monthInput.toInt() == 6 -> monthInput = "06"
                monthInput.toInt() == 7 -> monthInput = "07"
                monthInput.toInt() == 8 -> monthInput = "08"
                monthInput.toInt() == 9 -> monthInput = "09"
                monthInput.toInt() == 10 -> monthInput = "10"
                monthInput.toInt() == 11 -> monthInput = "11"
                monthInput.toInt() == 12 -> monthInput = "12"
            }
            when {
                dayInput.toInt() == 1 -> dayInput = "01"
                dayInput.toInt() == 2 -> dayInput = "02"
                dayInput.toInt() == 3 -> dayInput = "03"
                dayInput.toInt() == 4 -> dayInput = "04"
                dayInput.toInt() == 5 -> dayInput = "05"
                dayInput.toInt() == 6 -> dayInput = "06"
                dayInput.toInt() == 7 -> dayInput = "07"
                dayInput.toInt() == 8 -> dayInput = "08"
                dayInput.toInt() == 9 -> dayInput = "09"
            }

            dateSet.setText("$dayInput\\$monthInput\\$year")
        }, year, month, day)

        dpd.show()

    }
}