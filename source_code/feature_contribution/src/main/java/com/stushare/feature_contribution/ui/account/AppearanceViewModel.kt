package com.stushare.feature_contribution.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stushare.feature_contribution.data.AppPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class AppearanceViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = AppPreferences(application)

    // StateFlow để UI lắng nghe
    val isDarkTheme = prefs.isDarkTheme.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val language = prefs.languageCode.stateIn(viewModelScope, SharingStarted.Eagerly, "vi")
    val fontScale = prefs.fontScale.stateIn(viewModelScope, SharingStarted.Eagerly, 1.0f)

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch { prefs.setDarkTheme(isDark) }
    }

    fun setLanguage(code: String) {
        viewModelScope.launch {
            prefs.setLanguage(code)
            // Lưu ý: Việc đổi ngôn ngữ cần cấu hình thêm ở MainActivity để áp dụng ngay
        }
    }

    fun setFontScale(scale: Float) {
        viewModelScope.launch { prefs.setFontScale(scale) }
    }
}