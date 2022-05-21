package com.kivous.attendanceApp

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*


class StudentGiveAttendanceActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
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

        ivClose.setOnClickListener {
            finish()
        }

        val d: String = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())
        etDate.setText(d)

        getBranchAndSubject(tfBranch, tfSubject)

        val currentUser = auth.currentUser!!.uid
        db.collection("extra_info").document("code").get().addOnSuccessListener { tasks ->
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
                        db.collection(cl).document(currentUser).set(info)

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

        getInfo(etName, etRoll, tfBranch, currentUser)
        swipeLayout.setOnRefreshListener {
            getInfo(etName, etRoll, tfBranch, currentUser)
            checkLocation(btnPost)
            swipeLayout.isRefreshing = false
        }
        checkLocation(btnPost)


    }

    @SuppressLint("MissingPermission")
    private fun checkLocation(btnPost: Button) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude

                    db.collection("extra_info").document("location").get()
                        .addOnSuccessListener { t ->
                            try {
                                val lat2 = t.get("latitude").toString().toDouble()
                                val long2 = t.get("longitude").toString().toDouble()
                                val radius = t.get("radius").toString().toDouble()
                                if (isInCampus(lat2, long2, lat, long, radius)) {
                                    btnPost.visibility = View.VISIBLE
                                } else {
                                    btnPost.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        "You are not in classroom",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this,
                                    "null location set by teacher",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "null location", Toast.LENGTH_SHORT)
                        .show()
                }


            }
    }

    private fun getInfo(name: EditText, roll: EditText, branch: EditText, currentUser: String) {
        db.collection("users").document(currentUser).get().addOnSuccessListener { tasks ->
            name.setText(tasks.get("name").toString())
            roll.setText(tasks.get("classroll").toString())
            branch.setText(tasks.get("branch2").toString())
        }
    }


    private fun getBranchAndSubject(branch: AutoCompleteTextView, subject: AutoCompleteTextView) {
        db.collection("extra_info")
            .document("array").get()
            .addOnSuccessListener { t ->
                val branchOptions: ArrayList<String> = t.get("branch") as ArrayList<String>
                val branchAdapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, branchOptions)
                branch.setAdapter(branchAdapter)
                val subjectOptions: ArrayList<String> = t.get("subject") as ArrayList<String>
                val subjectAdapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectOptions)
                subject.setAdapter(subjectAdapter)
            }
    }

    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Radius of the earth
        val latDistance = Math.toRadians(abs(lat2 - lat1))
        val lonDistance = Math.toRadians(abs(lon2 - lon1))
        val a = (sin(latDistance / 2) * sin(latDistance / 2)
                + (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
                * sin(lonDistance / 2) * sin(lonDistance / 2)))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        var distance = r * c * 1000 // distance in meter
        distance = distance.pow(2.0)
        return sqrt(distance)
    }

    private fun isInCampus(
        setLat: Double,
        setLong: Double,
        yourLat: Double,
        yourLong: Double,
        radius: Double
    ): Boolean {
        return getDistance(setLat, setLong, yourLat, yourLong) <= radius
    }


}