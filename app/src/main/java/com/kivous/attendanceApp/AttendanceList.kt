package com.kivous.attendanceApp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.kivous.attendanceApp.models.Attendance

class AttendanceList : AppCompatActivity() {
    lateinit var adapter: AttendanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_list)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)
        val ivClose: ImageView = findViewById(R.id.iv_close)
        val cl = intent.getStringExtra("collection").toString()
        setUpRecycleView(cl)
        ivClose.setOnClickListener {
            finish()
        }

    }

    private fun setUpRecycleView(cl: String) {
        val collection = db.collection(cl)
        val query = collection.orderBy("roll", Query.Direction.ASCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Attendance>().setQuery(query, Attendance::class.java)
                .build()

        adapter = AttendanceAdapter(recyclerViewOptions)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
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