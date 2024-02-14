package com.example.empowerplant.utils

import io.sentry.ITransaction
import io.sentry.Scope
import io.sentry.Sentry
import io.sentry.SpanStatus
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException


class CartController {
    private val products = JSONArray()

    fun checkout() {
        /*val checkoutTransaction = Sentry.startTransaction("checkout [android]", "http.client")
        checkoutTransaction.operation = "http"
        Sentry.configureScope { scope ->
            scope.transaction = checkoutTransaction
        }*/
        val currentTransaction = Sentry.getSpan()
        val headers = "application/json; charset=utf-8"
        val json = headers?.toMediaTypeOrNull()
        val domain = getEmpowerPlantDomain()
        val endpoint = "$domain/checkout"

        val body = this.products.toString().toRequestBody(json)

        val request = Request.Builder().url(endpoint).post(body).build()
        val client = OkHttpClient()

        var resJSON = JSONArray()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    processDeliveryItem(currentTransaction as ITransaction)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Sentry.captureException(e)
                e.printStackTrace()
            }
        })
    }

    private fun getEmpowerPlantDomain() : String {
        var domain = null;
        return "https://application-monitoring-flask-dot-sales-engineering-sf.appspot.com"
    }

    private fun processDeliveryItem(checkoutTransaction: ITransaction) {
        val processDeliverySpan = checkoutTransaction.startChild("task", "process delivery")
        try {
            throw ItemDeliveryProcessException("Failed to init delivery workflow")
        } catch (e: Exception) {
            processDeliverySpan.throwable = e
            processDeliverySpan.status = SpanStatus.INTERNAL_ERROR
            Sentry.captureException(e)
        }
        if (processDeliverySpan.status != SpanStatus.INTERNAL_ERROR) {
            processDeliverySpan.status = SpanStatus.OK
        }
        processDeliverySpan.finish()
    }

    fun addItem(item: Any) {
        this.products.put(item)
    }
}

internal class ItemDeliveryProcessException(message: String?) : RuntimeException(message)