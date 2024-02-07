package com.kasamo.kasamo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val back = findViewById<ImageButton>(R.id.logback)
        val done = findViewById<Button>(R.id.donelogin)
        val ema = findViewById<EditText>(R.id.emaillog)
        val emz = ema.text.toString()
        val shname = findViewById<EditText>(R.id.shopnamelog).text
        back.setOnClickListener {
            startActivity(Intent(applicationContext, signup::class.java))
        }
        done.setOnClickListener {
           if (ema.text.length == 0){
               ema.setError("this field is required");
           }//ends is
            else {
               val prefs = applicationContext.getSharedPreferences("account", Context.MODE_PRIVATE)
               val editor  = prefs.edit()
               editor.putString("email", ema.text.toString())
               editor.putString("shopname", shname.toString())
               editor.apply()
               println("my login details are "+prefs.getString("email", "")+prefs.getString("shopname", ""))
               startActivity(Intent(applicationContext, website::class.java))
               //Toast.makeText(applicationContext, "please fill in all the details", Toast.LENGTH_LONG).show()
            }//ends else
       }//ends on click

    }//ends oncreate
}//ends class