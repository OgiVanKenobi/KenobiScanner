package com.example.kenobiscanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main Activity that is started when app starts
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scan_button.setOnClickListener { openScanActivity() }
    }

    private fun openScanActivity() {
        val scanActivityIntent = Intent(this, ScanActivity::class.java)
        startActivity(scanActivityIntent)
    }
}
