package com.example.kenobiscanner

import android.app.Activity
import android.content.Intent
import android.hardware.camera2.*
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
class ScanActivity : Activity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        initViews()
        createCameraSource()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeDetector.release()
        cameraSource.release()
        cameraSource.stop()
    }

    private fun initViews() {
        //Without setting the renderer app crashes
        cameraView.setRenderer(GLRenderer())

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
    }

    private fun createCameraSource(cameraFacing: Int = CAMERA_FACING_BACK) {
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(CAMERA_PREVIEW_WIDTH, CAMERA_PREVIEW_HEIGHT)
            .setRequestedFps(CAMERA_PREVIEW_FPS)
            .setFacing(cameraFacing)
            .build()

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(p0: SurfaceHolder) {
                startCamera()
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) { }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val scannedCodes: SparseArray<Barcode> =
                    detections?.detectedItems as SparseArray<Barcode>
                if (scannedCodes.size() > 0) {
                    barcodeDetector.release()
                    openResultActivity(scannedCodes)
                }
            }
        })
    }

    private fun startCamera() {
        if (PermissionUtils.checkCameraPermission(this@ScanActivity)) {
            cameraSource.start(cameraView.holder)
        }
    }

    private fun openResultActivity(scannedCodes: SparseArray<Barcode>) {
        val resultActivityIntent = Intent(this@ScanActivity, ResultActivity::class.java)
        resultActivityIntent.putExtra(SCAN_RESULTS_KEY, scannedCodes.valueAt(0).displayValue)
        startActivity(resultActivityIntent)
        finish()
    }

    private fun initListeners() {
        flipCameraButton.setOnClickListener {
            flipCamera()
        }
    }

    private fun flipCamera() {
        cameraSource.stop()
        cameraSource.release()

        try {
            if (isCameraFacingBack()) {
                createCameraSource(CAMERA_FACING_FRONT)
            } else {
                createCameraSource(CAMERA_FACING_BACK)
            }

            startCamera()
        } catch (e: CameraAccessException) {
            Toast.makeText(this, getString(R.string.error_cannot_access_camera), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun isCameraFacingBack() = cameraSource.cameraFacing == CAMERA_FACING_BACK

    companion object {
        const val SCAN_RESULTS_KEY = "scanResults"
        const val CAMERA_PREVIEW_WIDTH = 1600
        const val CAMERA_PREVIEW_HEIGHT = 1024
        const val CAMERA_PREVIEW_FPS = 25f
        const val CAMERA_FACING_BACK = CameraSource.CAMERA_FACING_BACK
        const val CAMERA_FACING_FRONT = CameraSource.CAMERA_FACING_FRONT
    }
}
