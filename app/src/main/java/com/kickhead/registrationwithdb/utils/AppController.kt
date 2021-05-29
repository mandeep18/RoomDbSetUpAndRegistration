package com.kickhead.registrationwithdb.utils

import android.app.Application
import android.content.res.Configuration
import android.util.Log

class AppController:Application() {

    companion object{
        val TAG:String = AppController::class.java.simpleName
        var appController:AppController? = null
    }

    override fun onCreate() {
        super.onCreate()

        appController = this

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d(TAG, "onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(TAG, "onTrimMemory")
    }

    override fun onTerminate() {
        Log.d(TAG, "onTerminate")
        super.onTerminate()
    }

}