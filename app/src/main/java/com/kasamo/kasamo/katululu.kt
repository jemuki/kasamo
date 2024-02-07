package com.kasamo.kasamo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_katululu.*
class katululu : AppCompatActivity() {

    private val permission: String = Manifest.permission.READ_SMS
    private val requestCode: Int = 1
    var messages:  ArrayList<String> = ArrayList<String>()
    var loyals:  ArrayList<String> = ArrayList<String>()

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, edit::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_katululu)
        val back = findViewById<ImageButton>(R.id.katback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }
        val btn = findViewById<Button>(R.id.katbutton)
        btn.setOnClickListener {
            permission()
            println("kat button clicked")
        }




    }//ends oncreate

    //check the fpermissions
    fun permission(){
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission, android.Manifest.permission.SEND_SMS), requestCode)
            permission()
        } else {
            readSms()
            println("permissions are okay")
        }//ends else
    }//ends function


    //get the messages
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
            println("truly running")
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)
            val type = cursor.getString(typeColIdx)
            println("my haha message "+number+"+-+-+-+-+-+"+text)
            if (number == "MPESA"){
                   val c = Calendar.getInstance()
                    val day = c.get(Calendar.DATE)
                    val month = c.get(Calendar.MONTH)+1
                    val year = c.get(Calendar.YEAR).toString().takeLast(2)
                    val date = day.toString()+"/"+month.toString()+"/"+year
                println("my zipy y date "+date)
                if (text.contains(date)){
                    sendad(text)
                    println("my zipx date "+date)
                }else{
                 println("not today's message")
                }
            }//ends if its mpesa

        }// ends while loop
        cursor.close()

    }//ends function

    fun sendad(text: String) {
        if (text.contains("from")){
            println("received my from")
            val contact = text.replace(" ", "").split("from")
            println(" my current contact is "+contact)
            val mycontact = contact.get(1).take(12)
            val prefsz = getSharedPreferences("thead", Context.MODE_PRIVATE)
            val ad = prefsz.getString("thead", "")
            println("my ad is "+ad)
            val prefsx = getSharedPreferences("sent", Context.MODE_PRIVATE)
            val numsent = prefsx.getString("sent", null)
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

        }//ends if
    }//ends the send ad function



}//ends class