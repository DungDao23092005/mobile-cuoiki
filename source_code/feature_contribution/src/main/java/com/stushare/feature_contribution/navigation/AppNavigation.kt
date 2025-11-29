package com.stushare.feature_contribution.navigation

import com.stushare.feature_contribution.ui.account.SwitchAccountScreen
import com.stushare.feature_contribution.ui.account.TermsOfUseScreen
import com.stushare.feature_contribution.ui.account.PrivacyPolicyScreen
import com.stushare.feature_contribution.ui.account.ReportViolationScreen
import com.stushare.feature_contribution.ui.account.ContactSupportScreen
import com.stushare.feature_contribution.ui.account.AboutAppScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stushare.feature_contribution.ui.account.AccountSecurityScreen
import com.stushare.feature_contribution.ui.account.ChangePasswordScreen
import com.stushare.feature_contribution.ui.account.EditAttributeScreen
import com.stushare.feature_contribution.ui.account.NotificationSettingsScreen
import com.stushare.feature_contribution.ui.account.PersonalInfoScreen
import com.stushare.feature_contribution.ui.account.ProfileScreen
import com.stushare.feature_contribution.ui.account.ProfileViewModel
import com.stushare.feature_contribution.ui.account.AppearanceSettingsScreen
import com.stushare.feature_contribution.ui.account.SettingsScreen
import com.stushare.feature_contribution.ui.home.HomeScreen
import com.stushare.feature_contribution.ui.leaderboard.LeaderboardScreen
import com.stushare.feature_contribution.ui.leaderboard.LeaderboardViewModel
import com.stushare.feature_contribution.ui.noti.NotiScreen
import com.stushare.feature_contribution.ui.noti.NotiViewModel
import com.stushare.feature_contribution.ui.search.SearchScreen
import com.stushare.feature_contribution.ui.upload.UploadScreen
import com.stushare.feature_contribution.ui.upload.UploadViewModel

// Định nghĩa các màn hình (Routes)
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Upload : Screen("upload")
    object Noti : Screen("noti")
    object Profile : Screen("profile")
    object Leaderboard : Screen("leaderboard")
    object Settings : Screen("settings")
    object AccountSecurity : Screen("account_security")
    object PersonalInfo : Screen("personal_info")
    object ChangePassword : Screen("change_password")
    object EditPhone : Screen("edit_phone")
    object EditEmail : Screen("edit_email")
    object NotificationSettings : Screen("notification_settings")
    object AppearanceSettings : Screen("appearance_settings")
    object AboutApp : Screen("about_app")
    object ContactSupport : Screen("contact_support")
    object ReportViolation : Screen("report_violation")
    object SwitchAccount : Screen("switch_account")
    object TermsOfUse : Screen("terms_of_use")
    object PrivacyPolicy : Screen("privacy_policy")
}

@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // 1. Home
        composable(Screen.Home.route) {
            HomeScreen()
        }

        // 2. Search
        composable(Screen.Search.route) {
            SearchScreen()
        }

        // 3. Upload
        composable(Screen.Upload.route) {
            val viewModel: UploadViewModel = viewModel()
            UploadScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 4. Notification
        composable(Screen.Noti.route) {
            val viewModel: NotiViewModel = viewModel()
            NotiScreen(viewModel = viewModel)
        }

        // 5. Profile
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                viewModel = viewModel,
                onOpenSettings = { navController.navigate(Screen.Settings.route) },
                onOpenLeaderboard = { navController.navigate(Screen.Leaderboard.route) }
            )
        }

        // 6. Leaderboard
        composable(Screen.Leaderboard.route) {
            val viewModel: LeaderboardViewModel = viewModel()
            LeaderboardScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 7. Settings
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onAccountSecurityClick = { navController.navigate(Screen.AccountSecurity.route) },
                onNotificationSettingsClick = { navController.navigate(Screen.NotificationSettings.route) },
                onAppearanceSettingsClick = { navController.navigate(Screen.AppearanceSettings.route) },
                onAboutAppClick = { navController.navigate(Screen.AboutApp.route) },
                onContactSupportClick = { navController.navigate(Screen.ContactSupport.route) },
                onReportViolationClick = { navController.navigate(Screen.ReportViolation.route) },
                onSwitchAccountClick = { navController.navigate(Screen.SwitchAccount.route) }
            )
        }

        // 8. Account & Security
        composable(Screen.AccountSecurity.route) {
            AccountSecurityScreen(
                onBackClick = { navController.popBackStack() },
                onPersonalInfoClick = { navController.navigate(Screen.PersonalInfo.route) },
                onPhoneClick = { navController.navigate(Screen.EditPhone.route) },
                onEmailClick = { navController.navigate(Screen.EditEmail.route) },
                onPasswordClick = { navController.navigate(Screen.ChangePassword.route) },
                onDeleteAccountClick = { /* TODO: Show dialog confirm */ }
            )
        }

        // 9. Personal Info
        composable(Screen.PersonalInfo.route) {
            PersonalInfoScreen(onBackClick = { navController.popBackStack() })
        }

        // 10. Change Password
        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(onBackClick = { navController.popBackStack() })
        }

        // 11. Edit Phone
        composable(Screen.EditPhone.route) {
            val title = androidx.compose.ui.res.stringResource(com.stushare.feature_contribution.R.string.title_edit_phone)
            val label = androidx.compose.ui.res.stringResource(com.stushare.feature_contribution.R.string.label_edit_phone)
            
            EditAttributeScreen(
                title = title,
                label = label,
                initialValue = "0123456789",
                onBackClick = { navController.popBackStack() }
            )
        }

        // 12. Edit Email
        composable(Screen.EditEmail.route) {
            val title = androidx.compose.ui.res.stringResource(com.stushare.feature_contribution.R.string.title_edit_email)
            val label = androidx.compose.ui.res.stringResource(com.stushare.feature_contribution.R.string.label_edit_email)

            EditAttributeScreen(
                title = title,
                label = label,
                initialValue = "dungdao@test.com",
                onBackClick = { navController.popBackStack() }
            )
        }

        // 13. Notification Settings
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AppearanceSettings.route) {
        AppearanceSettingsScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.AboutApp.route) {
        AboutAppScreen(
            onBackClick = { navController.popBackStack() },
            onTermsClick = { navController.navigate(Screen.TermsOfUse.route) },
            onPrivacyClick = { navController.navigate(Screen.PrivacyPolicy.route) } 
        )
    }

    composable(Screen.TermsOfUse.route) {
        TermsOfUseScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.PrivacyPolicy.route) {
        PrivacyPolicyScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.ContactSupport.route) {
        ContactSupportScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.ReportViolation.route) {
        ReportViolationScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.SwitchAccount.route) {
        SwitchAccountScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
    }
}