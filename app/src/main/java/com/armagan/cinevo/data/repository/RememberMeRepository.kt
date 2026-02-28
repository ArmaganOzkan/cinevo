package com.armagan.cinevo.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore by preferencesDataStore(name = "remember_me")

@Singleton
class RememberMeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
        private val EMAIL_KEY = stringPreferencesKey("email")
    }

    val rememberMeFlow: Flow<Pair<Boolean, String?>> = context.dataStore.data
        .map { prefs ->
            val remember = prefs[REMEMBER_ME_KEY] ?: false
            val email = prefs[EMAIL_KEY]
            Pair(remember, email)
        }

    suspend fun saveRememberMe(remember: Boolean, email: String) {
        context.dataStore.edit { prefs ->
            prefs[REMEMBER_ME_KEY] = remember
            prefs[EMAIL_KEY] = if (remember) email else ""
        }
    }
    suspend fun clearRememberMe() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

}
