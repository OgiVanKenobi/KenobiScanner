package com.example.kenobiscanner

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_result.*

/**
 * Activity in charge of getting the results of the scan and displaying it on screen
 */
class ResultActivity : Activity() {

    private var scannedCode: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        scannedCode = intent?.extras?.get(ScanActivity.SCAN_RESULTS_KEY)
        resultTextView.text = scannedCode.toString()
        initListeners()
    }

    private fun initListeners() {
        open_link_button.setOnClickListener { openLinkInBrowser() }
        share_button.setOnClickListener { openShareIntent() }
        copy_button.setOnClickListener { placeDataInClipboard() }
        scan_again_button.setOnClickListener { openScanActivity() }
    }

    private fun openLinkInBrowser() {
        val url = scannedCode.toString()
        if (android.util.Patterns.WEB_URL.matcher(url).matches()) {
            val openBrowserIntent = Intent(Intent.ACTION_VIEW)
            openBrowserIntent.data = Uri.parse(url)
            startActivity(openBrowserIntent)
        } else {
            Toast.makeText(this, getString(R.string.no_link_toast_text), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openShareIntent() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, scannedCode.toString())
        shareIntent.type = "text/plain"
        startActivity(shareIntent)
    }

    private fun placeDataInClipboard() {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipboardData = ClipData.newPlainText("scanned text", scannedCode.toString())
        clipboard.setPrimaryClip(clipboardData)
        Toast.makeText(
            this, getString(R.string.copy_to_clipoboard_sucess_toast_text),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun openScanActivity() {
        val scanActivityIntent = Intent(this, ScanActivity::class.java)
        startActivity(scanActivityIntent)
        finish()
    }
}
