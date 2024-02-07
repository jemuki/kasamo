package com.kasamo.kasamo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.cardview.widget.CardView

class how : AppCompatActivity() {
    override fun onBackPressed() {  //this is when you tap the back button
        super.onBackPressed()
        startActivity(Intent(applicationContext,  edit::class.java))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how)

        val back = findViewById<ImageButton>(R.id.howback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }

        val sh = findViewById<CardView>(R.id.shopify)
        sh.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.shopify.com/blog/thank-your-customers#:~:text=Thank%20you%20for%20your%20recent,provide%20you%20with%20quality%20products.")
            startActivity(openURL)
        }

        val msg = findViewById<CardView>(R.id.slsmsg)
        msg.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.salesmessage.com/blog/sample-text-messages-to-customers")
            startActivity(openURL)
        }
        val power = findViewById<CardView>(R.id.power)
        power.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.powerdiary.com/blog/positive-sms-messages-for-clients/")
            startActivity(openURL)
        }
        val simple = findViewById<CardView>(R.id.simple)
        simple.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://simpletexting.com/how-to-write-sms-for-marketing/")
            startActivity(openURL)
        }
    }//ends class
}//ends oncreate