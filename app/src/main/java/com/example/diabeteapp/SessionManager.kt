// SessionManager.kt
package com.example.diabeteapp

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("DiabeteApp_Session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun createSession(userId: String) {
        sharedPreferences.edit {
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
        }
    }

    fun getCurrentUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession() {
        sharedPreferences.edit {
            clear()
        }
    }
}