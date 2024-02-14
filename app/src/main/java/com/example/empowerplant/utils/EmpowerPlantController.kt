package com.example.empowerplant.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.empowerplant.utils.database.PlantRoomDatabase
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.CountDownLatch

class EmpowerPlantController {

    private var products = JSONArray()

    @Composable
    fun fetchProducts() : JSONArray {
        val context = LocalContext.current
        //val db = Room.databaseBuilder(context, PlantRoomDatabase::class.java, "plants").build()
        val domain = getEmpowerPlantDomain()
        val endpoint = "$domain/products"

        val request = Request.Builder().url(endpoint).build()
        val client = OkHttpClient()

        var resJSON = JSONArray()

        var countDownLatch = CountDownLatch(1);
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseStr = response.body?.string()

                    if (responseStr != null && responseStr != "") {
                        var jsonArray = JSONArray(responseStr)
                        resJSON = jsonArray
                    }
                }
                countDownLatch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                countDownLatch.countDown()
            }
        })

        countDownLatch.await()
        this.products = resJSON
        return resJSON

    }

    fun getProducts() : JSONArray? {
        return if (this.products.length() == 0) null else this.products
    }

    @Override
    override fun toString() : String {
        return this.products.toString()
    }

    private fun getEmpowerPlantDomain() : String {
        var domain = null;
        return "https://application-monitoring-flask-dot-sales-engineering-sf.appspot.com"
    }
}