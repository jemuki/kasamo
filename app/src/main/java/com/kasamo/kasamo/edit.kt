package com.kasamo.kasamo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_editweb.*

class edit : AppCompatActivity() {

    override fun onBackPressed() {  //this is when you tap the back button
        super.onBackPressed()
    startActivity(Intent(applicationContext,  MainActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        animation2()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        animation2()
        val back = findViewById<ImageButton>(R.id.backimg)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        val prefs2x = getSharedPreferences("speech", Context.MODE_PRIVATE)
        if (prefs2x.getString("status", null) != null){
            val text = findViewById<TextView>(R.id.textView17)
            text.text = prefs2x.getString("status", "")
        }else{
            val textx  = findViewById<TextView>(R.id.textView17)
            textx.text= "on"
        }
//        val web = findViewById<CardView>(R.id.tomyweb)
//        web.setOnClickListener {
//            val prefszxz = getSharedPreferences("account", Context.MODE_PRIVATE)
//            if (prefszxz.getString("email", null) != null){
//                startActivity(Intent(applicationContext, website::class.java))
//            }
//            else{
//                startActivity(Intent(applicationContext, signup::class.java))
//            }
//        }
        val prefs = applicationContext.getSharedPreferences("thead", Context.MODE_PRIVATE)
        val prefsx = getSharedPreferences("sent", Context.MODE_PRIVATE)
        val edit = findViewById<EditText>(R.id.edittext)
        val done = findViewById<ImageButton>(R.id.sendimg)
        val thead = findViewById<TextView>(R.id.textView3)
        val sent = findViewById<TextView>(R.id.textView5)
        val payout = findViewById<TextView>(R.id.textView7)
        if (prefs.getString("thead", null) != null){
            thead.text = prefs.getString("thead", "")
        }//ends the if
        if (prefsx.getString("sent", null) != null){
            sent.text = prefsx.getString("sent", "")
            val s = prefsx.getString("sent", "")!!.toInt()
            val pay = 0.5 * s
            payout.text = "ksh "+ pay
        }//ends if
        done.setOnClickListener {
            val editor = prefs.edit()
            editor.putString("thead", edit.text.toString())
            editor.apply()
            thead.text = prefs.getString("thead", "")
            edit.text.clear()
        }


//        val toed = findViewById<CardView>(R.id.tomywebedit)
//        val prefsxzz = getSharedPreferences("account", Context.MODE_PRIVATE)
//        val em = prefsxzz.getString("email", null)
//        if (em == null){
//            toed.visibility = View.GONE
//        }
//        toed.setOnClickListener {
//            startActivity(Intent(applicationContext, editweb::class.java))
//        }

        val cl = findViewById<CardView>(R.id.call)
        cl.setOnClickListener {
            val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:254768992045"))
            startActivity(call)
        }

        val feed = findViewById<CardView>(R.id.feedx)
        feed.setOnClickListener {
            startActivity(Intent(applicationContext, feedback::class.java))
        }
        val howx = findViewById<CardView>(R.id.how)
        howx.setOnClickListener {
            startActivity(Intent(applicationContext, how::class.java))
        }
        val loyal = findViewById<CardView>(R.id.toloyal)
        loyal.setOnClickListener {
            startActivity(Intent(applicationContext, loyals::class.java))
        }
        val kat = findViewById<CardView>(R.id.tokat)
        kat.setOnClickListener {
            startActivity(Intent(applicationContext, katululu::class.java))
        }
        val speech = findViewById<CardView>(R.id.conf)
        speech.setOnClickListener {
            val speprefs = getSharedPreferences("speech", Context.MODE_PRIVATE)
            val seditor = speprefs.edit()
            if (speprefs.getString("status", null) != null){
                if (speprefs.getString("status", "") == "off"){
                    seditor.putString("status", "on")
                    seditor.apply()
                    finish();
                    startActivity(intent);
                }else{
                    seditor.putString("status", "off")
                    seditor.apply()
                    finish();
                    startActivity(intent)
                }
            }else{
                seditor.putString("status", "off")
                seditor.apply()
                finish()
                startActivity(intent)
            }
        }
    }//ends oncreate


    fun animation2(){
        val videoView = findViewById<VideoView>(R.id.videoView2)
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

    }

}//ends class