package com.kivous.attendanceApp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.kivous.attendanceApp.models.StudentAttendance


class StudentCheckAttendanceActivity : AppCompatActivity() {
    private lateinit var adapter: StudentCheckAttendanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_check_attendance)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)

        val ivClose: ImageView = findViewById(R.id.iv_close)
        val recyclerView: RecyclerView = findViewById(R.id.rv_student)

        val currentUser = auth.currentUser!!.uid
        setUpRecycleView(currentUser, recyclerView)
        ivClose.setOnClickListener {
            finish()
        }

    }

    private fun setUpRecycleView(cl: String, recyclerView: RecyclerView) {
        val query = db.collection(cl).orderBy("time", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<StudentAttendance>()
                .setQuery(query, StudentAttendance::class.java).build()

        adapter = StudentCheckAttendanceAdapter(recyclerViewOptions)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}