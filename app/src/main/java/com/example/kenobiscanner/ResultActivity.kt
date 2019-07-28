package com.example.kenobiscanner

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*

/**
 * Activity in charge of getting the results of the scan and displaying it on screen
 */
class ResultActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val scannedCode = intent?.extras?.get(ScanActivity.SCAN_RESULTS_KEY)
        resultTextView.text = scannedCode.toString()
    }
}
