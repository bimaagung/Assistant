package com.otetcode.assistant.Helper

import android.os.AsyncTask
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import java.net.InetSocketAddress
import java.net.Socket
import java.sql.Connection
import java.util.function.Consumer

class InternetCheck (private val consumer:Consumer): AsyncTask<Void, Void, Boolean>() {

    init {
        execute() // fungsi auto running untuk memanggil class
    }

    override fun doInBackground(vararg p0: Void?): Boolean {
        try {
            val sock = Socket()
            sock.connect(InetSocketAddress("google.com", 80), 1500)
            sock.close()
            return true
        }catch (e:Exception){
            return false
        }
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        consumer.accept(result)
    }

    interface Consumer{
        fun accept(isConnected: Boolean?)
    }
}

