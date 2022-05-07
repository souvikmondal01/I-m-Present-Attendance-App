package com.kivous.attendanceApp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class StudentDashBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dash_board)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)
        auth = FirebaseAuth.getInstance()

        val ivAccount: ImageView = findViewById(R.id.iv_account)
        val ivGiveAttendance: ImageView = findViewById(R.id.iv_give_attendance)
        val ivCheckAttendance: ImageView = findViewById(R.id.iv_check_attendance)
        val back: ImageView = findViewById(R.id.iv_back)

//        val token = getSharedPreferences("username", Context.MODE_PRIVATE)
//        val email = token.getString("login_email", " ")
//        setTextToActionBar(email)
        back.setOnClickListener {
            finish()
        }
        ivAccount.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        ivGiveAttendance.setOnClickListener {
            startActivity(Intent(this, CreatePost::class.java))
        }
        ivCheckAttendance.setOnClickListener {
            startActivity(Intent(this, CheckAttendance::class.java))
        }


//        fab.setOnClickListener {
//            val view = View.inflate(this, R.layout.profile_layout, null)
//
//            // function for open dialog box
//            dialogBoxProfile(view)
////            setTextToProfile(email, view)
//
//            val btnLogout: Button = view.findViewById(R.id.btn_logout)
//            btnLogout.setOnClickListener {
//                logOut(token)
//            }
//        }

//        val d: String = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())
////        val d: String = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date())
//        binding.etDate.setText(d)


    }

//    private fun setTextToActionBar(email: String?) {
//        val tvName: TextView = findViewById(R.id.tv_name2)
//        if (email != null) {
//            db.collection("users").document(email).get().addOnSuccessListener { tasks ->
//                tvName.text = tasks.get("name").toString()
//            }
//        }
//    }

//    private fun setTextToProfile(email: String?, view: View) {
//        val name: TextView = view.findViewById(R.id.tv_profile_name)
//        val email2: TextView = view.findViewById(R.id.tv_profile_email)
//        if (email != null) {
//            db.collection("users").document(email).get().addOnSuccessListener { tasks ->
//                name.text = tasks.get("name").toString()
//                email2.text = tasks.get("email").toString()
//            }
//        }
//    }

//    private fun logOut(token: SharedPreferences) {
//        token.edit().putString("login_email", " ").apply()
//        startActivity(Intent(this, StudentLogin::class.java))
//        finish()
//    }

//    private fun dialogBoxProfile(view: View) {
//        val builder = AlertDialog.Builder(this)
//        builder.setView(view)
//        val dialog = builder.create()
//        dialog.show()
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        dialog.window?.setGravity(Gravity.TOP)
//        dialog.window?.setWindowAnimations(R.style.PauseDialogAnimation)
//    }
}
