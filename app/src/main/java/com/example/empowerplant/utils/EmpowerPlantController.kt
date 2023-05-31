package com.example.empowerplant.utils

import android.util.Log
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import java.util.concurrent.CountDownLatch

class EmpowerPlantController {

    private var products = JSONArray()

    fun fetchProducts() : JSONArray {
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