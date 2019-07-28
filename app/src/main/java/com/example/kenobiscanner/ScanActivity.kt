package com.example.kenobiscanner

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.example.kenobiscanner.utils.GLRenderer
import com.example.kenobiscanner.utils.PermissionUtils
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_scan.*

/**
 * Scan Activity where all the scanning work is done
 */
class ScanActivity: AppCompatActivity() {

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource : CameraSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        //Without setting the renderer app crashes
        camera_view.setRenderer(GLRenderer())
        createCameraSource()
    }

    private fun createCameraSource() {
        barcodeDetector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1600, 1024)
            .build()

        camera_view.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(p0: SurfaceHolder?) {
                if (PermissionUtils.checkCameraPermission(this@ScanActivity)) {
                    cameraSource.start(camera_view.holder)
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
            }

            override fun surfaceDestroyed(p0: SurfaceHolder?) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                var scannedCodes: SparseArray<Barcode> = detections?.detectedItems as SparseArray<Barcode>
                if (scannedCodes.size() > 0) {
                    val resultActivityIntent = Intent(this@ScanActivity, ResultActivity::class.java)
                    resultActivityIntent.putExtra("scanResults", scannedCodes.valueAt(0).displayValue)
                    startActivity(resultActivityIntent)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeDetector.release()
        cameraSource.release()
        cameraSource.stop()
    }
}
