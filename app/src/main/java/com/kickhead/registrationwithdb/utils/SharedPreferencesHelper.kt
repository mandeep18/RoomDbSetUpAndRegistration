package com.kickhead.registrationwithdb.utils


object SharedPreferencesHelper {

    fun getInt(key:String, defValue:Int):Int{
        return SharedPreferencesManager.with(context = AppController.appController).getInt(key, defValue)
    }

    fun putInt(key: String, value:Int){
        SharedPreferencesManager.with(context = AppController.appController).edit().putInt(key, value).apply()
    }
    fun clearAllSharePreferences(){
        SharedPreferencesManager.with(context = AppController.appController).edit().clear().apply()
    }
}