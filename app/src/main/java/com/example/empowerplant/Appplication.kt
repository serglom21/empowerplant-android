package com.example.empowerplant

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration

open class Application(base: Context?) : ContextWrapper(base), ComponentCallbacks2 {

    open fun onCreate() : Unit {
        //super.onCreate()
    }

    override fun onConfigurationChanged(p0: Configuration) {
        TODO("Not yet implemented")
    }

    override fun onLowMemory() {
        TODO("Not yet implemented")
    }

    override fun onTrimMemory(p0: Int) {
        TODO("Not yet implemented")
    }


}