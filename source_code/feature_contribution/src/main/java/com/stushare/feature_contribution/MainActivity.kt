package com.stushare.feature_contribution

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stushare.feature_contribution.data.AppPreferences
import com.stushare.feature_contribution.navigation.AppNavigationGraph
import com.stushare.feature_contribution.navigation.Screen
import com.stushare.feature_contribution.ui.components.BottomNavBar
import com.stushare.feature_contribution.ui.theme.StuShareTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = AppPreferences(applicationContext)

        // 1. Lấy ngôn ngữ đã lưu NGAY LẬP TỨC (Blocking) để tránh nhấp nháy
        val initialLanguage = runBlocking { prefs.languageCode.first() }
        
        // Áp dụng ngôn ngữ ngay trước khi vẽ UI
        applyLanguage(initialLanguage)

        setContent {
            // Đọc các cài đặt (Flow)
            val isDarkTheme by prefs.isDarkTheme.collectAsState(initial = false)
            val fontScale by prefs.fontScale.collectAsState(initial = 1.0f)
            
            // Theo dõi thay đổi ngôn ngữ (nếu người dùng đổi trong settings)
            val languageCode by prefs.languageCode.collectAsState(initial = initialLanguage)

            // 2. Dùng LaunchedEffect để chỉ đổi ngôn ngữ khi giá trị `languageCode` THAY ĐỔI
            LaunchedEffect(languageCode) {
                applyLanguage(languageCode)
            }

            // Áp dụng Font Scale
            val currentDensity = LocalDensity.current
            val customDensity = Density(currentDensity.density, fontScale = fontScale)

            CompositionLocalProvider(LocalDensity provides customDensity) {
                // Áp dụng Theme
                StuShareTheme(darkTheme = isDarkTheme) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val showBottomBar = currentRoute in listOf(
                        Screen.Home.route, Screen.Search.route, Screen.Noti.route, Screen.Profile.route
                    )

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) BottomNavBar(navController = navController)
                        }
                    ) { innerPadding ->
                        AppNavigationGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun applyLanguage(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = getSystemService(LocaleManager::class.java)
            val currentAppLocales = localeManager.applicationLocales
            // Chỉ đổi nếu ngôn ngữ hiện tại khác với ngôn ngữ cài đặt (Tránh loop)
            if (currentAppLocales.isEmpty || currentAppLocales.get(0).language != languageCode) {
                localeManager.applicationLocales = LocaleList.forLanguageTags(languageCode)
            }
        } else {
            val appLocale = AppCompatDelegate.getApplicationLocales()
            if (appLocale.isEmpty || appLocale.get(0)?.language != languageCode) {
                val localeList = LocaleListCompat.forLanguageTags(languageCode)
                AppCompatDelegate.setApplicationLocales(localeList)
            }
        }
    }
}