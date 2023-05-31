package com.example.empowerplant.utils

import io.sentry.HttpStatusCodeRange
import java.util.concurrent.TimeUnit

import io.sentry.android.okhttp.SentryOkHttpInterceptor
import okhttp3.OkHttpClient

class RequestClient {

    @JvmField val client = OkHttpClient.Builder()
        .addInterceptor(SentryOkHttpInterceptor(
            captureFailedRequests = true,
            failedRequestStatusCodes = listOf(HttpStatusCodeRange(400, 599))
        ))
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getClient() : OkHttpClient {
        return client;
    }
}