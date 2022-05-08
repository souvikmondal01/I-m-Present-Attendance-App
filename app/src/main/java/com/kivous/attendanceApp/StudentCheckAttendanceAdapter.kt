package com.kivous.attendanceApp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.kivous.attendanceApp.models.StudentAttendance

class StudentCheckAttendanceAdapter(options: FirestoreRecyclerOptions<StudentAttendance>) :
    FirestoreRecyclerAdapter<StudentAttendance, StudentCheckAttendanceAdapter.AttendanceViewHolder>(
        options
    ) {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val subject: TextView = itemView.findViewById(R.id.tv_subject)
        val name: TextView = itemView.findViewById(R.id.tv_name_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.student_attendance_list, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: AttendanceViewHolder,
        position: Int,
        model: StudentAttendance
    ) {
        holder.date.text = model.date
        holder.subject.text = model.subject
        holder.name.text = model.name

    }
}