package com.kasamo.kasamo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Telephony
import android.speech.tts.TextToSpeech
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private val permission: String = Manifest.permission.READ_SMS
    private val requestCode: Int = 1
    var messages:  ArrayList<String> = ArrayList<String>()
    var loyals:  ArrayList<String> = ArrayList<String>()
    var counter = 0

    var pcounter = 0
    var tapcounter = 0
    var resumecounter = 0
    var player: MediaPlayer? = null
    private var tts: TextToSpeech? = null

    override fun onPause() {   //this is  when you press the menu button on your phone
        super.onPause()
        player?.stop()
    }

    override fun onBackPressed() {  //this is when you tap the back button
        super.onBackPressed()

        player?.stop()
    }

    override fun onResume() {
        super.onResume()
        animation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



            animation()
           permission()

        tts = TextToSpeech(this, this)

        val toedit = findViewById<FloatingActionButton>(R.id.fab)
        toedit.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }


    }//ends oncreate

    fun permission(){
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission, android.Manifest.permission.SEND_SMS), requestCode)
            permission()
        } else {
            val text = findViewById<TextView>(R.id.text1)
            object: CountDownTimer(1000, 1000) {

                override fun onTick(p0: Long) {
                    //text.text = (p0/1000).toString()
                    println("seconds remaining"+p0/1000)
                }
                override fun onFinish() {
                    readSms()
                    permission()
                }

            }.start()

        }//ends else
    }//ends function

      fun readSms() {
        Toast.makeText(applicationContext, "checking...", Toast.LENGTH_LONG).show()
        val numberCol = Telephony.TextBasedSmsColumns.ADDRESS
        val textCol = Telephony.TextBasedSmsColumns.BODY
        val typeCol = Telephony.TextBasedSmsColumns.TYPE // 1 - Inbox, 2 - Sent

        val projection = arrayOf(numberCol, textCol, typeCol)

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val numberColIdx = cursor!!.getColumnIndex(numberCol)
        val textColIdx = cursor.getColumnIndex(textCol)
        val typeColIdx = cursor.getColumnIndex(typeCol)

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)
            val type = cursor.getString(typeColIdx)
            println("my haha message "+number+"+-+-+-+-+-+"+text)
            if (number == "MPESA"){
                val prefs = getSharedPreferences("db", Context.MODE_PRIVATE)
                val previous = prefs.getString("message", null)
                val message = " +-+-+-+ $text "
                val sender = "$number"
                println("my message is "+message)
                println("my sender is "+sender)
                if (previous == null){
                    val myprefs = applicationContext.getSharedPreferences("db", Context.MODE_PRIVATE)
                    val editor = myprefs.edit()
                    editor.putString("message", message)
                    editor.apply()
                    sendad(message)
                }//ends if
                else {
                    if (message == prefs.getString("message", "")){
                        println("myprefs "+prefs.getString("message", ""))
                        Toast.makeText(applicationContext, "no new message yet", Toast.LENGTH_LONG).show()
                    }//ends if message
                    else{
                        sendad(message)
                        val editor = prefs.edit()
                        editor.putString("message", message)
                        editor.apply()
                        println("my new prefs "+prefs.getString("message", ""))
                    }//ends if equal else
                }//ends else
                break
            }//ends if its mpesa

        }// ends while loop
        cursor.close()
    }//ends function

    fun sendad(message: String) {
        println("my super message is "+message)

        if (message.contains("from")){
        val contact = message.replace(" ", "").split("from")
        println(" my zipx contact "+contact)
        val mycontact = contact.get(1).take(12)
        println("my contact is "+mycontact)
        val speech = contact.get(0).split("sh").get(1)
            val sprefs = getSharedPreferences("speech", Context.MODE_PRIVATE)
            if (sprefs.getString("status", null) != null){
                if (sprefs.getString("status", "") == "off"){
                    println("speech not allowed")
                }else{
                    speakOut(speech)
                    println("my superx speech is "+speech)
                }
            }else{
                speakOut(speech)
            }//ends ifelse

        println("my speech is "+speech)
        val prefsz = getSharedPreferences("thead", Context.MODE_PRIVATE)
        val ad = prefsz.getString("thead", "")
        println("my ad is "+ad)
        val prefsx = getSharedPreferences("sent", Context.MODE_PRIVATE)
        val numsent = prefsx.getString("sent", null)
        // on the below line we are creating a try and catch block

            //sendpermission()

            try {

            // on below line we are initializing sms manager.
            val smsManager: SmsManager = SmsManager.getDefault()

            // on below line we are sending text message.
            smsManager.sendTextMessage(mycontact, null, ad, null, null)
            val loyal = applicationContext.getSharedPreferences("loyal", Context.MODE_PRIVATE)
                if (loyal.getString("loyallist", null) != null){
                    loyal.getString("loyallist", "")?.let { loyals.add(it) }
                    println("my loyals 1 is "+loyals)
                }//ends if
                loyals.add(mycontact)
                val editorl = loyal.edit()
                editorl.putString("loyallist", loyals.toString())
                editorl.apply()
                println("my loyals 2 is "+loyals)
                println("my prefssx"+loyal.getString("loyallist", ""))
                loyals.clear()
            if (numsent == null){
                val editorx = prefsx.edit()
                editorx.putString("sent", "1")
                editorx.apply()
                println("still in the numsent")
            }//ends if
            else {
                val prefsxz = getSharedPreferences("sent", Context.MODE_PRIVATE)
                var sentxx = prefsxz.getString("sent", "")!!.toInt()
                println("current sents "+sentxx)
                val senty = sentxx + 1
                val editorz = prefsxz.edit()
                editorz.putString("sent", senty.toString())
                editorz.apply()
                println("my latest sent is "+senty+"=========="+prefsxz.getString("sent", ""))
            }//ends else
            // on below line we are displaying a toast message for message send,
            Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            // on catch block we are displaying toast message for error.
            Toast.makeText(applicationContext, "please add a message to be sent..."+e.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }//ends if contains
    }//ends send ad

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                println("iniatilized")
            }
        }
    }
    private fun speakOut(speech: String) {
        val text = speech!!.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }//ends on destroy


    fun animation(){
        val videoView = findViewById<VideoView>(R.id.videoView)
        //Creating MediaController
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        val uri: Uri = Uri.parse(("android.resource://" + getPackageName() + "/" + R.raw.anim))   // check your path

        //  videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.start()
        }

    }//ends function animation
}//ends class



