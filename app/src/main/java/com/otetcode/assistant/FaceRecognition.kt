package com.otetcode.assistant

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.otetcode.assistant.Helper.InternetCheck
import com.wonderkiln.camerakit.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_face_recognition.*

class FaceRecognition : AppCompatActivity() {

    lateinit var waitingDialog:AlertDialog

    override fun onResume() {
        super.onResume()
        camera_view.start()
    }

    override fun onPause() {
        super.onPause()
        camera_view.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_recognition)

        waitingDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage("Mohon menunggu")
            .show()

        btn_detect.setOnClickListener{
            camera_view.start()
            camera_view.captureImage()
        }

        camera_view.addCameraKitListener(object: CameraKitEventListener{
            override fun onVideo(p0: CameraKitVideo?) {

            }

            override fun onEvent(p0: CameraKitEvent?) {

            }

            override fun onImage(p0: CameraKitImage?) {
               var bitmap:Bitmap = p0!!.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, camera_view.width, camera_view.height, false)
                camera_view.stop()

                runDetector(bitmap)
            }

            override fun onError(p0: CameraKitError?) {

            }

        })

    }

    private fun runDetector(bitmap: Bitmap?) {
        val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap!!)

        InternetCheck(object: InternetCheck.Consumer{
            override fun accept(isConnected: Boolean?) {
                if (isConnected!!)
                {
                    val options: FirebaseVisionCloudImageLabelerOptions = FirebaseVisionCloudImageLabelerOptions.Builder()
                        .setMaxResults(1)
                        .build()

                    val detector: FirebaseVisionImageLabeler = FirebaseVision.getInstance().getCloudImageLabeler(options)

                    detector.detectInImage(image)


                }
            }

        })
    }
}
