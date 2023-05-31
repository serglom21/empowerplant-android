package com.example.empowerplant.utils

import android.util.Log
import io.sentry.Sentry

object Action {

    fun arithmeticException(){
        val num = 5 / 0;
    }

    fun stripPII(){
        val x: IntArray = intArrayOf(1, 2, 3)
        val num = x[-4]
    }

    fun handledError(){
        try {
            val x : IntArray = intArrayOf(1,2,3)
            val num = x[4]
        } catch (e: Exception) {
            Sentry.captureException(e)
        }
    }

    fun anrException(){
        try {
            Thread.sleep(20000);
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    fun nativeCrash(){
        System.loadLibrary("native-sample")
    }

    fun nativeMessage(){}
}