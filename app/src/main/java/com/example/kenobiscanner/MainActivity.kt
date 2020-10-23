package com.example.kenobiscanner

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.kenobiscanner.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main Activity that is started when app starts
 */
class MainActivity : Activity() {

    companion object {
        const val SCAN_REQUEST_CODE: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        version.text = "Version : " + BuildConfig.VERSION_NAME
        scan_button.setOnClickListener { openScanActivity() }
        privacyPolicyButton.setOnClickListener { openPrivacyPolicy() }
    }

    private fun openScanActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.checkCameraPermission(this)) {
                startScanActivity()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun openPrivacyPolicy() {
        val openBrowserIntent = Intent(Intent.ACTION_VIEW)
        openBrowserIntent.data = Uri.parse(getString(R.string.privacy_policy_web_url))
        startActivity(openBrowserIntent)
    }

    private fun askForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(CAMERA)) {
                showErrorDialog()
            }
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(R.string.permission_denied_error_text)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.change_permission_button_text)) { _, _ -> requestCameraPermission() }
            .setNegativeButton(getString(R.string.dismiss_button_text), null)
            .create()
            .show()
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), SCAN_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SCAN_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanActivity()
            } else {
                askForCameraPermission()
            }
        }
    }

    private fun startScanActivity() {
        val scanActivityIntent = Intent(this, ScanActivity::class.java)
        startActivity(scanActivityIntent)
    }
}
