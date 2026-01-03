package com.simats.warrantymaintenance.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "WarrantyMaintenancePrefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ROLE = "user_role"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserSession(context: Context, userId: Int, userName: String, email: String, role: String? = null) {
        val prefs = getPreferences(context)
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            role?.let { putString(KEY_USER_ROLE, it) }
            apply()
        }
    }

    fun getUserId(context: Context): Int {
        return getPreferences(context).getInt(KEY_USER_ID, -1)
    }

    fun getUserName(context: Context): String {
        return getPreferences(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun getUserEmail(context: Context): String {
        return getPreferences(context).getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun getUserRole(context: Context): String {
        return getPreferences(context).getString(KEY_USER_ROLE, "") ?: ""
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession(context: Context) {
        getPreferences(context).edit().clear().apply()
    }
}
