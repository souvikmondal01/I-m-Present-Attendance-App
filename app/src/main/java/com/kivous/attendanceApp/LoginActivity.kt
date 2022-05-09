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
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var isShowPass = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_800)

        val ivBackArrow: ImageView = findViewById(R.id.iv_back_arrow)
        val ivWhiteBackground: ImageView = findViewById(R.id.iv_white_background)
        val tvRegister: TextView = findViewById(R.id.tv_Register)
        val btnLogin: Button = findViewById(R.id.btn_login)
        val etEmail: EditText = findViewById(R.id.et_email)
        val etPassword: EditText = findViewById(R.id.et_password)
        val pbLogin: ProgressBar = findViewById(R.id.pb_login)
        val ivEye: ImageView = findViewById(R.id.iv_eye)
        val forgotPass: TextView = findViewById(R.id.tv_forgot_password)

        auth = FirebaseAuth.getInstance()
        val token = getSharedPreferences("username", Context.MODE_PRIVATE)

        ivBackArrow.setOnClickListener {
            finish()
        }
        ivWhiteBackground.setOnClickListener {
            closeKeyBoard()
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            when {
                etEmail.text.isEmpty() -> {
                    etEmail.error = "enter email"
                }
                etPassword.text.isEmpty() -> {
                    etPassword.error = "enter password"
                }
                else -> {
                    pbLogin.visibility = View.VISIBLE
                    val email = etEmail.text.toString().trim()
                    val password = etPassword.text.toString().trim()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                if (auth.currentUser!!.isEmailVerified) {
                                    auth = FirebaseAuth.getInstance()
                                    val currentUser = auth.currentUser!!.uid
                                    pbLogin.visibility = View.GONE

                                    db.collection("users").document(currentUser).get()
                                        .addOnSuccessListener { t ->
                                            when (t.getString("student")) {
                                                "1" -> {
                                                    val intent =
                                                        Intent(
                                                            this,
                                                            StudentDashboardActivity::class.java
                                                        )
                                                    startActivity(intent)
                                                }
                                                "0" -> {
                                                    val intent =
                                                        Intent(
                                                            this,
                                                            TeacherDashboardActivity::class.java
                                                        )
                                                    startActivity(intent)
                                                }
                                                else -> {
                                                    val intent =
                                                        Intent(this, LoginActivity::class.java)
                                                    startActivity(intent)
                                                }
                                            }
                                            val editor = token.edit()
                                            editor.putString("login_email", currentUser)
                                            editor.apply()
                                            finishAffinity()
                                        }
                                } else {
                                    pbLogin.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        "Please verify your email address",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                pbLogin.visibility = View.GONE
                                Toast.makeText(
                                    this, task.exception!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                }
            }

        }

        forgotPass.setOnClickListener {
            pbLogin.visibility = View.VISIBLE
            if (etEmail.text.isNotEmpty()) {
                auth.sendPasswordResetEmail(etEmail.text.toString()).addOnCompleteListener { t ->
                    if (t.isSuccessful) {
                        pbLogin.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Check your email to reset your password!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {
                        pbLogin.visibility = View.GONE
                        Toast.makeText(this, t.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                pbLogin.visibility = View.GONE
                etEmail.error = "enter email"
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

    fun showPassword(isShow: Boolean, et: EditText, iv: ImageView) {
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