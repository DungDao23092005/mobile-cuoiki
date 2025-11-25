package com.stushare.feature_contribution.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Tạo DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class AppPreferences(private val context: Context) {

    companion object {
        val THEME_KEY = booleanPreferencesKey("is_dark_theme")
        val LANGUAGE_KEY = stringPreferencesKey("language_code") // "vi" hoặc "en"
        val FONT_SCALE_KEY = floatPreferencesKey("font_scale")   // 1.0f (Thường), 0.85f (Nhỏ), 1.15f (Lớn)
    }

    // --- Đọc dữ liệu (Flow) ---
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[THEME_KEY] ?: false } // Mặc định là Sáng (false)

    val languageCode: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[LANGUAGE_KEY] ?: "vi" } // Mặc định Tiếng Việt

    val fontScale: Flow<Float> = context.dataStore.data
        .map { preferences -> preferences[FONT_SCALE_KEY] ?: 1.0f } // Mặc định 1.0

    // --- Ghi dữ liệu ---
    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { it[THEME_KEY] = isDark }
    }

    suspend fun setLanguage(code: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = code }
    }

    suspend fun setFontScale(scale: Float) {
        context.dataStore.edit { it[FONT_SCALE_KEY] = scale }
    }
}