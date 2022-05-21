package com.kivous.attendanceApp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class StudentDashboardActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        const val REQUEST_CODE = 101
    }

    @SuppressLint("MissingPermission", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_10)
        auth = FirebaseAuth.getInstance()

        val vAccount: View = findViewById(R.id.v_account)
        val ivGiveAttendance: ImageView = findViewById(R.id.iv_give_attendance)
        val ivCheckAttendance: ImageView = findViewById(R.id.iv_check_attendance)
        val vBack: View = findViewById(R.id.v_back)

        vBack.setOnClickListener {
            finish()
        }

        vAccount.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        ivGiveAttendance.setOnClickListener {
            startActivity(Intent(this, StudentGiveAttendanceActivity::class.java))
        }
        ivCheckAttendance.setOnClickListener {
            startActivity(Intent(this, StudentCheckAttendanceActivity::class.java))
        }

        requestPermission()

    }


    private fun requestPermission() {
        if (PermissionUtility.hasLocationPermissions(applicationContext)) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this app.",
                REQUEST_CODE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this app.",
                REQUEST_CODE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            requestPermission()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
