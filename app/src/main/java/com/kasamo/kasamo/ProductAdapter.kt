package com.kasamo.kasamo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject


class ProductAdapter(var context: Context, productList: ArrayList<ProductModel>):
    RecyclerView.Adapter<ProductAdapter .ViewHolder>(){

    var productList : List<ProductModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemproduct, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.fname) as TextView
        val price = holder.itemView.findViewById<TextView>(R.id.fprice)
        val loc = holder.itemView.findViewById<TextView>(R.id.desc)
        val image = holder.itemView.findViewById(R.id.fimage) as ShapeableImageView
        val before = holder.itemView.findViewById<ImageButton>(R.id.navbefore)
        val next = holder.itemView.findViewById<ImageButton>(R.id.navnext)
        val del = holder.itemView.findViewById<ImageButton>(R.id.delpro)
        var counter = 0
        val item = productList[position]
        if (item.image1.isEmpty()){
            name.text = item.name
            price.text = item.price
            loc.text = item.description
            image.visibility = View.GONE

        }//ends the if
       else {
            name.text = item.name
            price.text = item.price
            loc.text = item.description
            Picasso
                .with(context)
                .load(item.image1)
                .fit()
                .into(image);
            var listx: java.util.ArrayList<String> = java.util.ArrayList<String>()
            listx.add(item.image1)
            listx.add(item.image2)
            listx.add(item.image3)
            listx.add(item.image4)
            listx.add(item.image4)
            listx.add(item.image5)
            listx.add(item.image6)
            listx.add(item.image7)
            listx.add(item.image8)
            listx.add(item.image9)
            listx.add(item.image10)
            next.setOnClickListener {
             if (counter != 10){
                counter++
                 println("my counter 1 is "+counter)
                 if (listx[counter] != "blank"){
                    Picasso
                        .with(context)
                        .load(listx[counter])
                        .fit()
                        .into(image);
                 }else {
                     Toast.makeText(context, "no more images", Toast.LENGTH_LONG).show()
                     counter--
                     println("my counter 3 is "+counter)
                 }
             }else {
                 Toast.makeText(context, "no more images", Toast.LENGTH_LONG).show()
             }
            }//ends next on click
            before.setOnClickListener {
               if (counter != 0){
                counter --
                   println("my counter 2 is "+counter)
                if (listx[counter] != "blank"){
                    Picasso
                        .with(context)
                        .load(listx[counter])
                        .fit()
                        .into(image);
                }
               }else
               {
                   Toast.makeText(context, "no more images", Toast.LENGTH_LONG).show()
               }
            }

        }//ends the else
        val id = item.id
        del.setOnClickListener {
            delete(id)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    //we will call this function on Loopj response
    fun setProductListItems(productList: List<ProductModel>){
        this.productList = productList
        notifyDataSetChanged()
    }

    fun delete(id: String) {
        val client = AsyncHttpClient(true, 80, 443)
        val data = JSONObject()
        data.put("id", id)
        val condata = StringEntity(data.toString())
        client.post(context, "http://coding.co.ke/jereson/deletekasamo",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(context,  "product deleted successfully", Toast.LENGTH_LONG).show()
       //             finish();
//                    startActivity(getIntent());

                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(context, "Something went wrong "+statusCode, Toast.LENGTH_LONG).show()
                }


            })//ends the client.post

    }//ends delete

}//end class
