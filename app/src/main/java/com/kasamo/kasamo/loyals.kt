package com.kasamo.kasamo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.kasamo.kasamo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_loyals.*


class loyals : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onBackPressed() {  //this is when you tap the back button
        super.onBackPressed()
        startActivity(Intent(applicationContext,  edit::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loyals)


        val back = findViewById<ImageButton>(R.id.loyalback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }


        val prefs = getSharedPreferences("loyal", Context.MODE_PRIVATE)
        if (prefs.getString("loyallist", null) != null){
        val numbers = prefs.getString("loyallist", "")?.replace("[", "")?.replace("]", "")?.replace(",", "")?.replace(" ", "+")?.split("+")
        println("my numbers is "+numbers)
            val top = numbers?.groupingBy { it }?.eachCount()
            println("top is "+top)
            val topsize = top?.size
            println("my top size is "+topsize)
            if (topsize != null) {
                if (topsize > 9){
                    val topten = top?.toList()?.sortedByDescending { it.second }?.take(10)
                    println("top ten is "+topten)
                    for (x in topten){
                        val padding = resources.getDimension(R.dimen.text_padding).toInt()
                        val textColor = ContextCompat.getColor(this, R.color.text_color)
                        val tv_dynamic = TextView(this)
                        tv_dynamic.textSize = 20f
                        tv_dynamic.setPadding(padding, padding, padding, padding)
                        tv_dynamic.setTextColor(textColor)
                        tv_dynamic.text = "$x"
                        tv_dynamic.layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        // add TextView to LinearLayout
                        rootLayout.addView(tv_dynamic)
                    }//ends for loop
                }else {
                    val padding = resources.getDimension(R.dimen.text_padding).toInt()
                    val textColor = ContextCompat.getColor(this, R.color.text_color)
                    val dynamic = TextView(this)
                    dynamic.textSize = 20f
                    dynamic.setPadding(padding, padding, padding, padding)
                    dynamic.setTextColor(textColor)
                    dynamic.text = "in this page you will see the top ten most loyal customers"
                    dynamic.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    // add TextView to LinearLayout
                    rootLayout.addView(dynamic)


                    val tv_dynamic = TextView(this)
                    tv_dynamic.textSize = 20f
                    tv_dynamic.setPadding(padding, padding, padding, padding)
                    tv_dynamic.setTextColor(textColor)
                    tv_dynamic.text = "the feature is available upon reaching 10 different customers, threshold."
                    tv_dynamic.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    // add TextView to LinearLayout
                    rootLayout.addView(tv_dynamic)

                }
            }//ends if topsize main

        }//ends the main if

    }//ends oncreate
}//ends class

