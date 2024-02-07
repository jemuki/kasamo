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

class signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val back = findViewById<ImageButton>(R.id.signback)
        val done = findViewById<Button>(R.id.donesignup)
        val ema = findViewById<EditText>(R.id.email)
        val emz = ema.text.toString()
        val sname = findViewById<EditText>(R.id.shopname).text
        val loginz = findViewById<Button>(R.id.tologin)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }
        loginz.setOnClickListener {
            startActivity(Intent(applicationContext, login::class.java))
        }
        done.setOnClickListener {
           if (ema.text.length == 0){
           ema.setError("this field is required")

           }//ends if
            else {
            println("in the else ")
               val prefs = applicationContext.getSharedPreferences("account", Context.MODE_PRIVATE)
               val editor = prefs.edit()
               editor.putString("email", ema.text.toString())
               editor.putString("shopname", sname.toString())
               editor.apply()
               println("my shopname is haha "+prefs.getString("shopname", "")+prefs.getString("email", ""))
               startActivity(Intent(applicationContext, website::class.java))
           }
         }//ends ooclick
    }//ends oncreate
}//ends class