package com.example.kenobiscanner

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResultActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scannedCode = intent?.extras?.get("scanResults")
        Toast.makeText(this, scannedCode.toString(), Toast.LENGTH_LONG).show()
    }
}
