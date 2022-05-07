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


class CheckAttendance : AppCompatActivity() {
    lateinit var adapter: StudentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_attendance)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)

        val ivClose: ImageView = findViewById(R.id.iv_close)
        val currentUser = auth.currentUser!!.uid
        setUpRecycleView(currentUser)
        ivClose.setOnClickListener {
            finish()
        }
    }

    private fun setUpRecycleView(cl: String) {
        val collection = db.collection(cl)
        val query = collection.orderBy("time", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<StudentAttendance>()
                .setQuery(query, StudentAttendance::class.java).build()

        adapter = StudentAdapter(recyclerViewOptions)
        val recyclerView: RecyclerView = findViewById(R.id.rv_student)
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