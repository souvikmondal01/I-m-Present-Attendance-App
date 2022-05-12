package com.kivous.attendanceApp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class RegisterActivity : AppCompatActivity() {
    private var isShowPass = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_800)

        val ivBackArrow: ImageView = findViewById(R.id.iv_back_arrow)
        val vBackArrow: View = findViewById(R.id.v_back_arrow)
        val ivWhiteBackground: ImageView = findViewById(R.id.iv_white_background)
        val tvLogin: TextView = findViewById(R.id.tv_login)
        val vLogin: View = findViewById(R.id.v_login)
        val btnStudentRegister: Button = findViewById(R.id.btn_student_register)
        val etEmail: EditText = findViewById(R.id.et_email)
        val etName: EditText = findViewById(R.id.et_name)
        val etRoll: EditText = findViewById(R.id.et_roll)
        val etBranch: EditText = findViewById(R.id.et_Branch)
        val etPassword: EditText = findViewById(R.id.et_password)
        val pbRegister: ProgressBar = findViewById(R.id.pb_register)
        val ivEye: ImageView = findViewById(R.id.iv_eye)

        ivBackArrow.setOnClickListener {
            finish()
        }
        vBackArrow.setOnClickListener {
            finish()
        }

        tvLogin.setOnClickListener {
            finish()
        }
        vLogin.setOnClickListener {
            finish()
        }

        ivWhiteBackground.setOnClickListener {
            closeKeyBoard()
        }
        btnStudentRegister.setOnClickListener {
            when {
                etName.text.isEmpty() -> {
                    etName.error = "enter name"
                }
                etRoll.text.isEmpty() -> {
                    etRoll.error = "enter university roll"
                }
                etBranch.text.isEmpty() -> {
                    etBranch.error = "enter branch"
                }
                etEmail.text.isEmpty() -> {
                    etEmail.error = "enter email"
                }
                etPassword.text.isEmpty() -> {
                    etPassword.error = "enter password"
                }
                etPassword.length() < 6 -> {
                    etPassword.error = "weak password"
                }
                else -> {
                    pbRegister.visibility = View.VISIBLE

                    val name = etName.text.toString().trim()
                    val roll = etRoll.text.toString().trim()
                    val branch = etBranch.text.toString().trim()
                    val email = etEmail.text.toString().trim()
                    val password = etPassword.text.toString().trim()

                    val user = hashMapOf(
                        "name" to name,
                        "roll" to roll,
                        "branch" to branch,
                        "email" to email,
                        "student" to "1",
                        "classroll" to "",
                        "branch2" to "",
                        "gender" to "",
                        "dob" to "",
                    )

                    val user2 = hashMapOf(
                        "name" to name,
                        "roll" to roll,
                        "branch" to branch,
                        "email" to email,
                        "student" to "0",
                        "classroll" to "",
                        "branch2" to "",
                        "gender" to "",
                        "dob" to "",
                    )

                    val users = db.collection("users")

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                auth.currentUser!!.sendEmailVerification()
                                    .addOnCompleteListener { task2 ->
                                        if (task2.isSuccessful) {
                                            val currentUser = auth.currentUser!!.uid
                                            pbRegister.visibility = View.GONE
                                            if (etRoll.text.toString() == "hitteacher") {
                                                users.document(currentUser).set(user2)
                                            } else {
                                                users.document(currentUser).set(user)
                                            }
                                            startActivity(
                                                Intent(
                                                    this,
                                                    LoginActivity::class.java
                                                )
                                            )
                                            Toast.makeText(
                                                this,
                                                "Registered successfully. Please check your email for verification",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                            finishAffinity()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                task2.exception!!.message,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }

                            } else {
                                pbRegister.visibility = View.GONE
                                Toast.makeText(
                                    this, task.exception!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

        ivEye.setOnClickListener {
            isShowPass = !isShowPass
            LoginActivity().showPassword(isShowPass, etPassword, ivEye)
        }
        LoginActivity().showPassword(isShowPass, etPassword, ivEye)

    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}