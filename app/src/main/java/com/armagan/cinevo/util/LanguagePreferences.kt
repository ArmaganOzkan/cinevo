package com.armagan.cinevo.util

import android.content.Context
import android.content.SharedPreferences

class LanguagePreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("dizibox_prefs", Context.MODE_PRIVATE)

    fun getLanguage(): String {
        return prefs.getString("selected_language", "tr") ?: "tr"
    }

    fun setLanguage(language: String) {
        prefs.edit().putString("selected_language", language).apply()
    }
}