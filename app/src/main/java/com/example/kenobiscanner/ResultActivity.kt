package com.example.kenobiscanner

import android.app.Activity
import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.example.kenobiscanner.utils.DataUtils
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
        updateDataInfo()
        initListeners()
    }

    private fun updateDataInfo() {
        resultTextView.text = scannedCode.toString()
        when (DataUtils.getScannedDataType(scannedCode.toString())) {
            DataUtils.ScannedDataType.URL -> {
                data_type_text_view.text = DataUtils.ScannedDataType.URL.value
                data_type_icon.setImageResource(R.drawable.data_type_url)
                action_button.text = getString(R.string.open_link_button_text)
                action_button.setOnClickListener { openLinkInBrowser(scannedCode.toString()) }
            }
            DataUtils.ScannedDataType.PHONE_NUMBER -> {
                data_type_text_view.text = DataUtils.ScannedDataType.PHONE_NUMBER.value
                data_type_icon.setImageResource(R.drawable.data_type_phone)
                action_button.text = getString(R.string.dial_number_button_text)
                action_button.setOnClickListener { dialNumber(scannedCode.toString()) }
            }
            DataUtils.ScannedDataType.EMAIL -> {
                data_type_text_view.text = DataUtils.ScannedDataType.EMAIL.value
                data_type_icon.setImageResource(R.drawable.data_type_email)
                action_button.text = getString(R.string.send_email_button_text)
                action_button.setOnClickListener { sendEmail(scannedCode.toString()) }
            }
            DataUtils.ScannedDataType.PLAIN_TEXT -> {
                data_type_text_view.text = DataUtils.ScannedDataType.PLAIN_TEXT.value
                data_type_icon.setImageResource(R.drawable.data_type_plain_text)
                action_button.text = getString(R.string.search_web_button_text)
                action_button.setOnClickListener { searchWeb(scannedCode.toString()) }
            }
        }
    }

    private fun initListeners() {
        share_button.setOnClickListener { openShareIntent() }
        copy_button.setOnClickListener { placeDataInClipboard() }
        scan_again_button.setOnClickListener { openScanActivity() }
    }

    private fun openLinkInBrowser(url: String) {
        if (android.util.Patterns.WEB_URL.matcher(url).matches()) {
            val openBrowserIntent = Intent(Intent.ACTION_VIEW)
            openBrowserIntent.data = Uri.parse(url)
            startActivity(openBrowserIntent)
        } else {
            Toast.makeText(this, getString(R.string.no_link_toast_text), Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(phoneNumber)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun sendEmail(emailAdress: String) {
        val uri = Uri.parse("mailto::$emailAdress")
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            type = "*/*"
            data = uri
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

   private fun searchWeb(query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
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
