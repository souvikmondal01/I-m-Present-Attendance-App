package com.kivous.attendanceApp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TeacherTakeAttendanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_attendance)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)
        val tfBranch: AutoCompleteTextView = findViewById(R.id.tf_branch)
        val tfSubject: AutoCompleteTextView = findViewById(R.id.tf_subject)
        val etDate: EditText = findViewById(R.id.et_date)
        val ivClose: ImageView = findViewById(R.id.iv_close)
        val btnList: Button = findViewById(R.id.btn_see_attendance_list)
        val etCode: EditText = findViewById(R.id.et_code)
        val setCode: Button = findViewById(R.id.btn_set)
        val swipeLayout: SwipeRefreshLayout = findViewById(R.id.swipeLayout)
        val btnPost: Button = findViewById(R.id.btn_post)
        val etName: EditText = findViewById(R.id.et_name)
        val etRoll: EditText = findViewById(R.id.et_roll)

        ivClose.setOnClickListener {
            finish()
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
                    val intent = Intent(this, TeacherAttendanceListActivity::class.java)
                    intent.putExtra("collection", collection)
                    startActivity(intent)
                }
            }
        }
        etDate.setOnClickListener {
            datePicker(etDate)
        }
        val d: String = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())
        etDate.setText(d)

        val branchOptions = resources.getStringArray(R.array.branch)
        val branchAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, branchOptions)
        tfBranch.setAdapter(branchAdapter)

        val subjectOptions = resources.getStringArray(R.array.subject)
        val subjectAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectOptions)
        tfSubject.setAdapter(subjectAdapter)

        setCode.setOnClickListener {
            val code = etCode.text.toString().trim()
            if (code.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val collection = db.collection("code")
                val codeHash = hashMapOf("code" to code)
                collection.document("code").set(codeHash)
            } else {
                etCode.error = "enter code"
            }
            getCode()
        }
        swipeLayout.setOnRefreshListener {
            getCode()
            swipeLayout.isRefreshing = false
        }
        getCode()

        btnPost.setOnClickListener {
            val name = etName.text.toString().trim()
            val roll = etRoll.text.toString().trim()
            val date = etDate.text.toString().trim()
            val branch = tfBranch.editableText.toString().trim()
            val subject = tfSubject.editableText.toString().trim()

            when {
                etName.text.isEmpty() -> {
                    etName.error = "enter name"
                }
                etRoll.text.isEmpty() -> {
                    etRoll.error = "enter roll"
                }
                tfBranch.text.isEmpty() -> {
                    tfBranch.error = "select branch"
                }
                tfSubject.text.isEmpty() -> {
                    tfSubject.error = "select subject"
                }
                else -> {
                    val info = hashMapOf(
                        "name" to name,
                        "roll" to roll,
                        "date" to date,
                        "branch" to branch,
                        "subject" to subject
                    )
                    val cl = "$date$branch$subject"
                    val attendanceInfo = db.collection(cl)
                    attendanceInfo.document().set(info).addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, t.exception!!.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }

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
                monthInput.toInt() == 1 -> monthInput = "Jan"
                monthInput.toInt() == 2 -> monthInput = "Feb"
                monthInput.toInt() == 3 -> monthInput = "Mar"
                monthInput.toInt() == 4 -> monthInput = "Apr"
                monthInput.toInt() == 5 -> monthInput = "May"
                monthInput.toInt() == 6 -> monthInput = "Jun"
                monthInput.toInt() == 7 -> monthInput = "Jul"
                monthInput.toInt() == 8 -> monthInput = "Aug"
                monthInput.toInt() == 9 -> monthInput = "Sep"
                monthInput.toInt() == 10 -> monthInput = "Oct"
                monthInput.toInt() == 11 -> monthInput = "Nov"
                monthInput.toInt() == 12 -> monthInput = "Dec"
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

            dateSet.setText("$dayInput $monthInput, $year")
        }, year, month, day)

        dpd.show()

    }

    private fun getCode() {
        val codeCollection = db.collection("code")
        codeCollection.document("code").get().addOnSuccessListener { tasks ->
            val tvCode: TextView = findViewById(R.id.tv_code)
            tvCode.text = tasks.get("code").toString()

        }
    }

}