package com.armagan.cinevo.ui.theme

import android.app.Application
import androidx.lifecycle.ViewModel
import com.armagan.cinevo.util.ThemePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    private val themePreference = ThemePreference(application)

    private val _isDarkTheme = MutableStateFlow(themePreference.isDarkTheme())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun toggleTheme(enabled: Boolean) {
        themePreference.setDarkTheme(enabled)
        _isDarkTheme.value = enabled
    }
}
