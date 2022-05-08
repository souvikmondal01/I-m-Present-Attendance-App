package com.kivous.attendanceApp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class StudentRegister : AppCompatActivity() {
    private var isShowPass = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_register)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_800)

        val ivBackArrow: ImageView = findViewById(R.id.iv_back_arrow)
        val ivWhiteBackground: ImageView = findViewById(R.id.iv_white_background)
        val tvLogin: TextView = findViewById(R.id.tv_login)
        val btnStudentRegister: Button = findViewById(R.id.btn_student_register)
        val etEmail: EditText = findViewById(R.id.et_email)
        val etName: EditText = findViewById(R.id.et_name)
        val etRoll: EditText = findViewById(R.id.et_roll)
        val etBranch: EditText = findViewById(R.id.et_Branch)
        val etPassword: EditText = findViewById(R.id.et_password)
        val pbRegister: ProgressBar = findViewById(R.id.pb_register)
        val ivEye: ImageView = findViewById(R.id.iv_eye)

//        val token = getSharedPreferences("username", Context.MODE_PRIVATE)

        ivBackArrow.setOnClickListener {
            finish()
        }
        tvLogin.setOnClickListener {
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


//                    users.whereEqualTo("email", email).get().addOnSuccessListener { e ->
//                        if (e.isEmpty) {
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
                                                Toast.makeText(
                                                    this,
                                                    "Registered successfully. Please check your email for verification",
                                                    Toast.LENGTH_LONG
                                                )
                                                    .show()
                                                val intent =
                                                    Intent(
                                                        this,
                                                        StudentLogin::class.java
                                                    )
                                                startActivity(intent)
                                            } else {
                                                users.document(currentUser).set(user)
                                                Toast.makeText(
                                                    this,
                                                    "Registered successfully. Please check your email for verification",
                                                    Toast.LENGTH_LONG
                                                )
                                                    .show()
                                                val intent =
                                                    Intent(
                                                        this,
                                                        StudentLogin::class.java
                                                    )
                                                startActivity(intent)
                                            }
//                                                val editor = token.edit()
//                                                editor.putString("login_email", currentUser)
//                                                editor.apply()
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

//                        }
//                        else {
//                            Toast.makeText(this, "You are already registered", Toast.LENGTH_SHORT)
//                                .show()
//                            val intent = Intent(this, StudentLogin::class.java)
//                            startActivity(intent)
//                        }
//                    }

                }
            }

        }



        ivEye.setOnClickListener {
            isShowPass = !isShowPass
            showPassword(isShowPass, etPassword, ivEye)
        }
        showPassword(isShowPass, etPassword, ivEye)


    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showPassword(isShow: Boolean, et: EditText, iv: ImageView) {
        if (isShow) {
            et.transformationMethod = HideReturnsTransformationMethod.getInstance()
            iv.setImageResource(R.drawable.ic_visibility)
        } else {
            et.transformationMethod = PasswordTransformationMethod.getInstance()
            iv.setImageResource(R.drawable.ic_visibility_off)
        }
        et.setSelection(et.text.toString().length)
    }
}