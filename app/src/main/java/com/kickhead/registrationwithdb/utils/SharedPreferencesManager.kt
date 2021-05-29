package com.kickhead.registrationwithdb.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager private constructor(context: Context)  {
    private val preferences: SharedPreferences = context.getSharedPreferences(SharedPrefConstant.PREF_LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
    companion object {
        private var singleton: SharedPreferencesManager? = null

        fun with(context: AppController?): SharedPreferencesManager {
            if (singleton == null) {
                synchronized(lock = SharedPreferencesManager::class.java) {
                    if (singleton == null)
                        singleton = Builder(context = context).build()
                }
            }

            return singleton!!
        }
    }

    fun getString(key: String, defValue: String?): String? = preferences.getString(key, defValue)

    fun getBoolean(key: String, defValue: Boolean): Boolean = preferences.getBoolean(key, defValue)

    fun getInt(key: String, defValue: Int): Int = preferences.getInt(key, defValue)

    fun getFloat(key: String, defValue: Float): Float = preferences.getFloat(key, defValue)

    @SuppressLint("CommitPrefEdits")
    fun edit(): SharedPreferences.Editor = preferences.edit()

    operator fun contains(key: String): Boolean = preferences.contains(key)

    private class Builder(context: Context?) {
        private val context: Context

        init {
            if (context == null)
                throw IllegalArgumentException("Context must not be null.")

            this.context = context.applicationContext
        }


        fun build(): SharedPreferencesManager = SharedPreferencesManager(context = context)
    }
}