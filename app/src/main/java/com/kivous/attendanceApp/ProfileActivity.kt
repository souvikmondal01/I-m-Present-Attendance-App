package com.kivous.attendanceApp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)

        val vBack: View = findViewById(R.id.v_back)
        val vLogOut: View = findViewById(R.id.v_logout)
        val tfBranch: AutoCompleteTextView = findViewById(R.id.tf_branch_profile)
        val tfGender: AutoCompleteTextView = findViewById(R.id.tf_gender_profile)
        val dob: EditText = findViewById(R.id.et_birth_day)
        val name: EditText = findViewById(R.id.et_name)
        val uniroll: EditText = findViewById(R.id.et_uniroll)
        val roll: EditText = findViewById(R.id.et_roll)
        val email: EditText = findViewById(R.id.et_email)
        val vUpdate: View = findViewById(R.id.v_update)
        val pb: ProgressBar = findViewById(R.id.pb_profile)
        val swipeLayout: SwipeRefreshLayout = findViewById(R.id.swipeLayout)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid

        vBack.setOnClickListener {
            finish()
        }
        dob.setOnClickListener {
            datePicker(dob)
        }
        vUpdate.setOnClickListener {
            pb.visibility = View.VISIBLE
            val name2 = name.text.toString().trim()
            val uniroll2 = uniroll.text.toString().trim()
            val roll2 = roll.text.toString().trim()
            val branch2 = tfBranch.text.toString().trim()
            val gender2 = tfGender.text.toString().trim()
            val dob2 = dob.text.toString().trim()

            val profileInfo = hashMapOf(
                "name" to name2,
                "roll" to uniroll2,
                "classroll" to roll2,
                "branch2" to branch2,
                "gender" to gender2,
                "dob" to dob2
            )
            try {
                if (isInternetConnection()) {
                    db.collection("users").document(currentUser)
                        .update(profileInfo as Map<String, Any>).addOnSuccessListener {
                            pb.visibility = View.GONE
                        }
                }
            } catch (e: Exception) {
                pb.visibility = View.GONE
                Toast.makeText(this, "internet connection is not available", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        db.collection("users").document(currentUser).get().addOnSuccessListener { tasks ->
            name.setText(tasks.get("name").toString())
            uniroll.setText(tasks.get("roll").toString())
            roll.setText(tasks.get("classroll").toString())
            tfBranch.setText(tasks.get("branch2").toString())
            tfGender.setText(tasks.get("gender").toString())
            dob.setText(tasks.get("dob").toString())
            email.setText(tasks.get("email").toString())
        }

        swipeLayout.setOnRefreshListener {
            db.collection("users").document(currentUser).get().addOnSuccessListener { tasks ->
                name.setText(tasks.get("name").toString())
                uniroll.setText(tasks.get("roll").toString())
                roll.setText(tasks.get("classroll").toString())
                tfBranch.setText(tasks.get("branch2").toString())
                tfGender.setText(tasks.get("gender").toString())
                dob.setText(tasks.get("dob").toString())
                email.setText(tasks.get("email").toString())
            }
            swipeLayout.isRefreshing = false
        }

        db.collection("extra_info")
            .document("array").get()
            .addOnSuccessListener { t ->
                val branchOptions: ArrayList<String> = t.get("branch") as ArrayList<String>
                val branchAdapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, branchOptions)
                tfBranch.setAdapter(branchAdapter)
                val genderOptions: ArrayList<String> = t.get("gender") as ArrayList<String>
                val genderAdapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, genderOptions)
                tfGender.setAdapter(genderAdapter)
            }

        val token = getSharedPreferences("username", Context.MODE_PRIVATE)
        vLogOut.setOnClickListener {
            logOut(token)
        }
    }

    private fun isInternetConnection(): Boolean {
        val connectivityManager =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting
    }

    private fun logOut(token: SharedPreferences) {
        token.edit().putString("login_email", " ").apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }


    @SuppressLint("SetTextI18n")
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