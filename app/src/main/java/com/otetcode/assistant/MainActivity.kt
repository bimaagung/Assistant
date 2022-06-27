package com.otetcode.assistant

import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.RemoteControlClient
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.service.media.MediaBrowserService
import android.speech.RecognitionService
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import java.util.jar.Manifest
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var tts:TextToSpeech? = null
    private var mSpeechRecognizer: SpeechRecognizer? = null
    private var keeper:String = ""
    private var textBalasan:String = ""
    private lateinit var afChangeListener: AudioManager.OnAudioFocusChangeListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var am = this.getSystemService(Context.AUDIO_SERVICE)
        // Request audio focus for playback


        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            var intent = Intent(this, FaceRecognition::class.java)
            startActivity(intent)
            finish()
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        var soundBeep = getSystemService(Context.AUDIO_SERVICE)  as AudioManager
        soundBeep.setStreamMute(AudioManager.STREAM_SYSTEM, true)

        tts = TextToSpeech(this, this)

        checkPermissionVoiceCommand()

//        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        var mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        var speechRecignizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecignizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecignizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ROOT)
        speechRecignizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 15000)
        speechRecignizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 15000);
        speechRecignizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 15000);
        speechRecignizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        mSpeechRecognizer.setRecognitionListener(object : RecognitionListener {

            override fun onBeginningOfSpeech() {
                // TODO Auto-generated method stub
                println("onBeginningOfSpeech")
                soundBeep!!.setStreamMute(AudioManager.STREAM_SYSTEM, true)


            }

            override fun onBufferReceived(arg0: ByteArray) {
                // TODO Auto-generated method stub
                println("onBufferReceived")
                Toast.makeText(this@MainActivity, "Aktif", Toast.LENGTH_LONG).show()

            }

            override fun onEndOfSpeech() {
                // TODO Auto-generated method stub
              //  mSpeechRecognizer.startListening(speechRecignizerIntent)
                println("onEndOfSpeech")

            }

            override fun onError(arg0: Int) {
                // TODO Auto-generated method stub
                println("onError")
                soundBeep!!.setStreamMute(AudioManager.STREAM_SYSTEM, true)
                GlobalScope.launch(context = Dispatchers.Main) {
                    println("launched coroutine 1")
                    Thread.sleep(2000)
                    println("Here after a delay of 5 seconds")
                    mSpeechRecognizer.startListening(speechRecignizerIntent)
                }
            }

            override fun onEvent(arg0: Int, arg1: Bundle) {
                // TODO Auto-generated method stub
                println("onEvent")

            }

            override fun onPartialResults(partialResults: Bundle) {
                // TODO Auto-generated method stub
                println("onPartialResults")

            }

            override fun onReadyForSpeech(params: Bundle) {
                // TODO Auto-generated method stub
                println("onReadyForSpeech")

            }

            override fun onResults(bundle: Bundle) {
                println("onResults")
                soundBeep!!.setStreamMute(AudioManager.STREAM_SYSTEM, false)
                val matchesFound = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                if (matchesFound != null) {
                    keeper = matchesFound!!.get(0)
                    Toast.makeText(this@MainActivity, keeper.decapitalize(), Toast.LENGTH_LONG).show()

                    if(keeper.decapitalize().equals("siapa namamu")) {
                        textBalasan = "Riani Lestari"
                        textSpeech(textBalasan)
                        GlobalScope.launch(context = Dispatchers.Main) {
                            var timeWord = textBalasan.length * 100
                            Thread.sleep(timeWord.toLong())
                            println("Here after a delay of 4 seconds")
                            mSpeechRecognizer.startListening(speechRecignizerIntent)
                        }
                    }
                    else if(keeper.decapitalize().equals("siapa namaku")) {
                        textBalasan = "Bima Agung Setya Budi"
                        textSpeech(textBalasan)
                        GlobalScope.launch(context = Dispatchers.Main) {
                            var timeWord = textBalasan.length * 100
                            Thread.sleep(timeWord.toLong())
                            println("Here after a delay of 4 seconds")
                            mSpeechRecognizer.startListening(speechRecignizerIntent)
                        }
                    }
                    else if(keeper.decapitalize().equals("makan apa hari ini")) {
//                       var yIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"))
//                        startActivity(yIntent)
                        textBalasan = "Makan gongso"
                        textSpeech(textBalasan)
                        GlobalScope.launch(context = Dispatchers.Main) {
                            var timeWord = textBalasan.length * 100
                            Thread.sleep(timeWord.toLong())
                            println("Here after a delay of 4 seconds")
                            mSpeechRecognizer.startListening(speechRecignizerIntent)
                        }
                    }
                    else if(keeper.decapitalize() == "riani"){
                        textBalasan = "Iya Bucin"
                        textSpeech(textBalasan)
                        GlobalScope.launch(context = Dispatchers.Main) {
                            var timeWord = textBalasan.length * 100
                            Thread.sleep(timeWord.toLong())
                            println("Here after a delay of 2 seconds")
                            mSpeechRecognizer.startListening(speechRecignizerIntent)
                        }
                    }
                    else if(keeper.decapitalize() == "putar lagu dari YouTube"){
                        textBalasan = "Iya bos"
                        textSpeech(textBalasan)
                        GlobalScope.launch(context = Dispatchers.Main) {
                            var timeWord = textBalasan.length * 100
                            Thread.sleep(timeWord.toLong())
                            println("Here after a delay of 2 seconds")
                            mSpeechRecognizer.startListening(speechRecignizerIntent)
                            soundBeep!!.setStreamMute(AudioManager.STREAM_SYSTEM, false)
                            var yIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=SlPhMPnQ58k&list=PLK-euiEs1S7sf5kU0KJuqQ3hewNKYBXW5"))
                            startActivity(yIntent)
                        }
                    }
                    else{
                        textBalasan = "Aku tidak tau maksudmu"
                        textSpeech(textBalasan)
                        GlobalScope.launch(context = Dispatchers.Main) {
                            var timeWord = textBalasan.length * 100
                            Thread.sleep(timeWord.toLong())
                            println("Here after a delay of 2 seconds")
                            //0,1321022727272727 per kata
                            mSpeechRecognizer.startListening(speechRecignizerIntent)
                        }
                    }
                }

            }

            override fun onRmsChanged(rmsdB: Float) {
                // TODO Auto-generated method stub
                println("onRmsChanged")


            }

        })

        mSpeechRecognizer.startListening(speechRecignizerIntent)
        keeper = ""

//        parentRelativeLayout.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
//                when (motionEvent?.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        mSpeechRecognizer.startListening(speechRecignizerIntent)
//                        keeper = ""
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        mSpeechRecognizer.stopListening()
//                    }
//                }
//
//                return false
//            }
//        })

        //Animasi Mata
        var animasiMata = mata_robot_1ayout.getDrawable() as AnimationDrawable
        animasiMata.start()




    }




//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when(requestCode){
//            REQUEST_CODE_SPEECH_INPUT -> {
//                if(resultCode == Activity.RESULT_OK && null != data){
//                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//                    var hasilSTT = result[0]
//                    textAwal.text = hasilSTT
//
//                    speakSTT()
//
//                    if(result[0].equals("Siapa namamu")) {
//                       var yIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"))
//                        startActivity(yIntent)
//                        textSpeech("Nama saya riani lestari")
//                    }else{
//                        textSpeech("Aku tidak tau maksudmu")
//                    }
//                }
//            }
//        }
//    }

    fun checkPermissionVoiceCommand() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this@MainActivity, RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.getPackageName(), null)
                intent.data = uri
                this.startActivity(intent)
            }
        }
    }

    fun textSpeech(hasilText: String){
        var text = hasilText

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        else
        {
            val hash = HashMap<String, String>()
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_NOTIFICATION.toString())
            tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, hash)
        }
    }

    override fun onInit(status: Int) {

        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.getDefault())

            if(result != TextToSpeech.LANG_MISSING_DATA || result != TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "Text to speech mendukung", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onStop() {

        println("onStop")
        var soundBeepPause = getSystemService(Context.AUDIO_SERVICE)  as AudioManager
        soundBeepPause.setStreamMute(AudioManager.STREAM_SYSTEM, false)
        super.onStop()
    }

    override fun onPause() {
        var soundBeepPause = getSystemService(Context.AUDIO_SERVICE)  as AudioManager
        soundBeepPause.setStreamMute(AudioManager.STREAM_SYSTEM, false)
        println("onPause")
        super.onPause()
    }

    public override fun onDestroy() {
        println("onDestroy")
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()
    }
}
