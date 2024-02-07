package com.kasamo.kasamo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import kotlinx.android.synthetic.main.activity_website.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class feedback : AppCompatActivity() {
    override fun onBackPressed() {  //this is when you tap the back button
        super.onBackPressed()
        startActivity(Intent(applicationContext,  edit::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        val back = findViewById<ImageButton>(R.id.feedbackback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }
        val feed = findViewById<EditText>(R.id.thefeed)
        val send = findViewById<Button>(R.id.sendfeed)
        send.setOnClickListener {
            if (feed.text.length == 0){
                feed.setError("please enter a valid feedback")
            }else {
                val client = AsyncHttpClient(true, 80, 443)
                val data = JSONObject()
                data.put("feedback", feed.text)
                val condata = StringEntity(data.toString())

                client.post(this, "http://coding.co.ke/jereson/kasamofeedback",
                    condata, "application/json",
                    object: JsonHttpResponseHandler() {
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            response: JSONObject?
                        ) {
                            Toast.makeText(applicationContext, "feedback sent successfully", Toast.LENGTH_LONG).show()
                           feed.text.clear()
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
            }//ends else
        }//ends on click
    }//ends oncreate
}//ends class