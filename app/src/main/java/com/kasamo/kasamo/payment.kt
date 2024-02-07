package com.kasamo.kasamo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Telephony
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject
import java.util.*

class payment : AppCompatActivity() {

    lateinit var amountx:String
    lateinit var paidzx:String
     var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
      //  val check = findViewById<Button>(R.id.btncheck)
        val sent1 = findViewById<TextView>(R.id.textViewx1)
        val amount1 = findViewById<TextView>(R.id.textViewx2)
        val edu = findViewById<TextView>(R.id.edu)
        val  prefs2 = getSharedPreferences("personal", Context.MODE_PRIVATE)
        val email = prefs2.getString("email", "")
        val prefs = getSharedPreferences("sent", Context.MODE_PRIVATE)
        val sent = prefs.getString("sent", "")
        sent1.text= sent
        val amount  = (0.5 * sent!!.toInt()).toString()

        if (amount.contains(".")){
            val amountxx = amount.split(".").get(0)
            amountx = amountxx
        }else {
            amountx = amount
        }

        amount1.text = amount

        edu.text = "please pay Ksh $amount to till number 9360193. While paying ensure the app is on to automatically clear you."
        val prox = findViewById<ProgressBar>(R.id.propay)
        val label = findViewById<TextView>(R.id.prolabel)
        prox.visibility = View.GONE
        label.visibility = View.GONE
        val till = findViewById<CardView>(R.id.tillno)
        till.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "9360193")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }//ends till

        object :CountDownTimer(1500, 750){
            override fun onTick(p0: Long) {
                println("confirm1 "+p0/1000)
            }
            override fun onFinish() {
                    confirm(sent)
            }
        }.start()



    }//ends oncreate


    fun confirm(sent: String) {
        println("looking for the message")
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
            println("x still on the run")
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)
            val type = cursor.getString(typeColIdx)
            println("my haha message "+number+"+-+-+-+-+-+"+text)
            if (number == "MPESA"){
                println("true, its mpesa")
                if (text.contains("FRANKLINE KIMATHI KIUMBE")){
                    val c = Calendar.getInstance()
                    val date = c.get(Calendar.DATE)
                    val month = c.get(Calendar.MONTH)+1
                    val year = c.get(Calendar.YEAR)
                    val adate = date.toString()+"/"+month.toString()+"/"
                    println("my date is "+adate)
                    if (text.contains(adate)){
                        val paid = text.replace(".", " ").replace("Ksh", " ").split(" ").get(4).toInt()
                        println("my paid is "+paid)
                        val  paidx = paid/0.5
                        println("my paid x "+paidx)
                        if (paidx <= sent.toInt() ){
                          val new =  sent.toInt()-paidx.toInt()
                            val prefsx = getSharedPreferences("sent", Context.MODE_PRIVATE)
                            val editorx = prefsx.edit()
                            editorx.putString("sent", new.toString())
                            editorx.apply()
                            edu.text = "payment confirmed, redirecting you back to the homepage shortly"
                            paidzx = paid.toString()
                            sendtodb()
                            break
                        }else{
                            val prefsy = getSharedPreferences("sent", Context.MODE_PRIVATE)
                            val editory = prefsy.edit()
                            editory.putString("sent", "0")
                            editory.apply()
                            paidzx = paid.toString()
                            edu.text = "payment confirmed, redirecting you back to the homepage shortly"
                            sendtodb()
                            break
                        }//if less or equal
                    }else{
                        println("date do not match")
                    }//ends if contains date
                }else{
                    println("not to FRANK")
                }//ends if contains frank
            }//ends if its mpesa

        }// ends while loop
        cursor.close()

    }//ends function confirm


    fun sendtodb() {
        object : CountDownTimer(10000, 1000){
            override fun onTick(p0: Long) {
                println("the remaining seconds"+p0/1000)
            }
            override fun onFinish() {
                val textx = findViewById<TextView>(R.id.edu)
                textx.text = "please connect to the internet"
                if (counter == 0){
                    sendtodb()
                }else{
                    println("zii manze")
                }
            }
        }.start()
        val pro = findViewById<ProgressBar>(R.id.propay)
        pro.visibility = View.VISIBLE
        val labelx = findViewById<TextView>(R.id.prolabel)
        labelx.visibility = View.VISIBLE
        val client = AsyncHttpClient(true, 80, 443)
        val c = Calendar.getInstance()
        val date = c.get(Calendar.DATE)
        val month = c.get(Calendar.MONTH)+1
        val year = c.get(Calendar.YEAR)
        val adate = date.toString()+"/"+month.toString()+"/"+year.toString()
        val data = JSONObject()
        data.put("amount", paidzx)
        data.put("date", adate)
        val condata = StringEntity(data.toString())
        //post it to api
        client.post(this, "https://jereson.pythonanywhere.com/paykasamo",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    counter++
                    println("my paycounter "+counter)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(applicationContext, "Something went wrong "+statusCode, Toast.LENGTH_LONG).show()
                }
            })//ends the client.post
    }//ends send to db

}//ends class
