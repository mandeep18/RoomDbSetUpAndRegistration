package com.kickhead.registrationwithdb.utils

import android.text.TextUtils
import android.util.Patterns

object PlatFormUtils {
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}