package com.kivous.attendanceApp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.kivous.attendanceApp.models.Attendance


class AttendanceAdapter(options: FirestoreRecyclerOptions<Attendance>) :
    FirestoreRecyclerAdapter<Attendance, AttendanceAdapter.AttendanceViewHolder>(options) {
    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attendanceName: TextView = itemView.findViewById(R.id.tv_student_name)
        val attendanceRoll: TextView = itemView.findViewById(R.id.tv_student_roll)
        val serial: TextView = itemView.findViewById(R.id.tv_serial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = AttendanceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.student_list_item, parent, false)
        )
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int, model: Attendance) {
        holder.attendanceName.text = model.name
        holder.attendanceRoll.text = model.roll
        holder.serial.text = (position + 1).toString()

    }
}