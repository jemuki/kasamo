package com.kasamo.kasamo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import kotlinx.android.synthetic.main.activity_website.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class website : AppCompatActivity() {
    lateinit var productList: ArrayList<ProductModel>
    lateinit var productAdapter: ProductAdapter
    lateinit var recyclerView: RecyclerView
    //store uris of picked images
    private var images: ArrayList<Uri?>? = null
    var pimg: ArrayList<String> = ArrayList<String>()
    //current position/index of selected images
    private var position = 0
    private lateinit var send: FloatingActionButton

    //request code to pick images
    private val PICK_IMAGES_CODE = 0

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null
    private var numimgs: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website)
        val back = findViewById<ImageButton>(R.id.webtoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }
        val sname = findViewById<TextView>(R.id.shopnameid)
        val slink = findViewById<TextView>(R.id.linkid)
        val zx = getSharedPreferences("account", Context.MODE_PRIVATE)
        val thename = zx.getString("shopname", "")
        val thename2 = SpannableString(thename)
        thename2.setSpan(UnderlineSpan(), 0 ,   thename2.length, 0)
        sname.text = thename
        val emz = zx.getString("email", "")
        val theemail = "http://coding.co.ke/jereson/kproducts/$emz"
        println("my email is "+theemail)
        val theemail2 = SpannableString(theemail)
        theemail2.setSpan(UnderlineSpan(), 0, theemail2.length, 0)
        slink.text = theemail2

        //set the link on the chooser
        val choser = findViewById<CardView>(R.id.sendlink)
        choser.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "$theemail")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }//ends chooser

        getproducts()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val send = findViewById<FloatingActionButton>(R.id.sendpronow)
        send.setOnClickListener {
            for (imageUri in images!!){
                if (imageUri != null) {
                    uploadImage(imageUri)
                }
            }//ends for
        }//ends send on click


        //init list
        images = ArrayList()
        pimg = ArrayList()
        //setup image switcher
        imageSwitcher.setFactory { ImageView(applicationContext) }

        //pick images clicking this button
        pickImagesBtn.setOnClickListener {
            pickImagesIntent()
        }

        //switch to next image clicking this button
        nextBth.setOnClickListener {
            if (position < images!!.size-1){
                position++
                imageSwitcher.setImageURI(images!![position])
            }
            else{
                //no more images
                Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
            }
        }
        //switch to previous image clicking this button
        backBtn.setOnClickListener {
            if (position > 0){
                position--
                imageSwitcher.setImageURI(images!![position])
            }
            else{
                //no more images
                Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImagesIntent(){
        val  intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_CODE){

            if (resultCode == Activity.RESULT_OK){

                if (data!!.clipData != null){
                    //picked multiple images
                    //get number of picked images
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count){
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        //add image to list
                        images!!.add(imageUri)
                    }//ends for
                    numimgs = count
                    //set first image from list to image switcher
                    imageSwitcher.setImageURI(images!![0])
                    position = 0;
                }
                else{
                    //picked single image
                    val imageUri = data.data
                    //set image to image switcher
                    imageSwitcher.setImageURI(imageUri)
                    position = 0;

                }//

            }

        }

    }//ends oncreate


    private fun uploadImage(imageUri: Uri) {
        if(imageUri != null){
            println("uri2"+imageUri)
            Toast.makeText(applicationContext, "uploading...", Toast.LENGTH_LONG).show()
            val ref = storageReference?.child("kasamo_products/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(imageUri!!)
                ?.addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            println("myurl is:"+imageUrl)
                            pimg.add(imageUrl)
                            var psize = pimg.size
                            var zip = numimgs
                            when {
                                pimg.size == numimgs ->{
                                    uploadall(pimg)
                                }
                            }//ends when

                            Toast.makeText(applicationContext, "uploading...", Toast.LENGTH_LONG).show()
                        }
                    })
                ?.addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(applicationContext, "please try again", Toast.LENGTH_LONG).show()
                })
        }else{
            Toast.makeText(this, "Please Upload a sample image of your product", Toast.LENGTH_SHORT).show()
        }
    }//ends upload image



    private fun uploadall(pimg: ArrayList<String>) {
        val prefs = getSharedPreferences("account", Context.MODE_PRIVATE)
        val email = prefs.getString("email", "")
        val snz = prefs.getString("shopname", "")
        val name = findViewById<EditText>(R.id.pname)
        val price = findViewById<EditText>(R.id.pprice)
        val desc = findViewById<EditText>(R.id.pdesc)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //val myLdt = LocalDateTime.of(year, month, day, ... )
        val adate = day.toString()+"/"+month.toString()+"/"+year.toString()
        val client = AsyncHttpClient(true, 80, 443)
        //prepare what you will post
        val data = JSONObject()
        //get username from edit text
        data.put("email", email)
        data.put("name", name.text.toString())
        data.put("price", price.text.toString())
        data.put("description", desc.text.toString())
        data.put("date", adate)
        data.put("shopname", snz)
        if (pimg.size >= 1){
            data.put("image1", this.pimg.get(0))
        }else {
            data.put("image1", "blank")
        }

        if (pimg.size >= 2){
            data.put("image2", this.pimg.get(1))
        }else {
            data.put("image2", "blank")
        }

        if (pimg.size >= 3){
            data.put("image3", this.pimg.get(2))
        }else {
            data.put("image3", "blank")
        }

        if (pimg.size >= 4){
            data.put("image4", this.pimg.get(3))
        }else {
            data.put("image4", "blank")
        }

        if (pimg.size >= 5){
            data.put("image5", this.pimg.get(4))
        }else {
            data.put("image5", "blank")
        }

        if (pimg.size >= 6){
            data.put("image6", this.pimg.get(5))
        }else {
            data.put("image6", "blank")
        }

        if (pimg.size >= 7){
            data.put("image7", this.pimg.get(6))
        }else {
            data.put("image7", "blank")
        }

        if (pimg.size >= 8){
            data.put("image8", this.pimg.get(7))
        }else {
            data.put("image8", "blank")
        }

        if (pimg.size >= 9){
            data.put("image9", this.pimg.get(8))
        }else {
            data.put("image9", "blank")
        }

        if (pimg.size >= 10){
            data.put("image10", this.pimg.get(9))
        }else {
            data.put("image10", "blank")
        }

        data.put("date", adate)

        val condata = StringEntity(data.toString())

        //post it to api
        client.post(this, "http://coding.co.ke/jereson/addkasamoproduct",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "product added successfully", Toast.LENGTH_LONG).show()
                    name.text.clear()
                    price.text.clear()
                    desc.text.clear()
                    finish();
                    startActivity(getIntent());
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

    }//ends upload all function




    fun getproducts(){
        val prefs = getSharedPreferences("account", Context.MODE_PRIVATE)
        val email = prefs.getString("email", "")

        recyclerView = findViewById(R.id.prorecycler)
        productList=ArrayList<ProductModel>()

        val client = AsyncHttpClient(true, 80, 443)
        productAdapter = ProductAdapter(applicationContext, productList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        val pro = findViewById<ProgressBar>(R.id.pro)
        client.get(this, "http://coding.co.ke/jereson/kasamoproducts/$email"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out cz.msebera.android.httpclient.Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<ProductModel>::class.java).toList()

                    if (List.size == 0){
                        Toast.makeText(applicationContext, "No products uploaded yet", Toast.LENGTH_LONG).show()
                        pro.visibility = View.GONE
                    }else {
                        productAdapter.setProductListItems(List)
                        pro.visibility = View.GONE
                    }
                }


                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out cz.msebera.android.httpclient.Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(applicationContext, "Try again "+statusCode, Toast.LENGTH_LONG).show()

                }
            })//ends handler

        recyclerView.adapter = productAdapter

    }//ends function
}//ends class