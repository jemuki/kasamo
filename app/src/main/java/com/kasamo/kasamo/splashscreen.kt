package com.kasamo.kasamo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)


        val launch = findViewById<TextView>(R.id.kasamo)
        launch.text = "kasamo"
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        launch.startAnimation(animationFadeIn)


        Handler().postDelayed(
            Runnable {
//                val preferences = getSharedPreferences("loyal", 0)
//                preferences.edit().remove("loyallist").commit()

                val prefs = getSharedPreferences("sent", Context.MODE_PRIVATE)
                val count = prefs.getString("sent", null)
                if (count != null){
                    if (prefs.getString("sent", "")!!.toInt() > 99) {
                            startActivity(Intent(applicationContext, payment::class.java))
                    }else {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                }else {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }//ends else
                finish()
            }, 3000
        )

    }//ends oncreate
}//ends class

//174379
//https://itexamanswers.net/ccna-1-v7-modules-1-3-basic-network-connectivity-and-communications-exam-answers.html
//https://justpaste.it/22nx8


//dialogue
//class StartGameDialogFragment : DialogFragment() {
//
//    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
//        return activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//            builder.setMessage(R.string.dialog_start_game)
//                .setPositiveButton(R.string.start,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // START THE GAME!
//                    })
//                .setNegativeButton(R.string.cancel,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                    })
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
//}