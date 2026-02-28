package com.armagan.cinevo.util

import android.content.Context
import android.content.SharedPreferences

class ThemePreference(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return prefs.getBoolean("dark_theme", false)
    }

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean("dark_theme", enabled).apply()
    }
}