package com.kasamo.kasamo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.gson.JsonObject
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.client.ResponseHandler
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject

class editweb : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editweb)
        val back  = findViewById<ImageButton>(R.id.webeditback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }
        val em = findViewById<EditText>(R.id.eemail)
        val sname = findViewById<EditText>(R.id.eshopname)
        val donee = findViewById<Button>(R.id.doneeditem)
        val donesn = findViewById<Button>(R.id.doneeditsn)

        val prefs = getSharedPreferences("account", Context.MODE_PRIVATE)
        //edit the email
        donee.setOnClickListener {
            if (em.text.length == 0 ){
                em.setError("please enter a valid email")
            }//ends if
            else {
            val email = prefs.getString("email", "")
            val client = AsyncHttpClient(true, 80, 443)
            val data = JSONObject()
            //get username from edit text
            data.put("oldemail", email)
            data.put("email", em.text)
            val condata = StringEntity(data.toString())

            //post it to api
            client.post(this, "http://coding.co.ke/jereson/editkasamoemail",
                condata, "application/json",
                object: JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        val editor = prefs.edit()
                        editor.putString("email", em.text.toString())
                        editor.apply()
                        Toast.makeText(applicationContext, "email edited successfully", Toast.LENGTH_LONG).show()
                        em.text.clear()
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
        }//ends done email

        donesn.setOnClickListener {
        if (sname.text.length == 0){
            sname.setError("please enter a valid shop name")
        }else {
            val em2 = prefs.getString("email", "")
            val client = AsyncHttpClient(true, 80, 443)
            val data = JSONObject()
            //get username from edit text
            data.put("email", em2)
            data.put("shopname", sname.text)
            val condata = StringEntity(data.toString())

            //post it to api
            client.post(this, "http://coding.co.ke/jereson/editkasamoshopname",
                condata, "application/json",
                object: JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        val editor = prefs.edit()
                        editor.putString("shopname", sname.text.toString())
                        editor.apply()
                        Toast.makeText(applicationContext, "shopname edited successfully", Toast.LENGTH_LONG).show()
                        sname.text.clear()
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
        }

    }//ends oncreate
}//ends class