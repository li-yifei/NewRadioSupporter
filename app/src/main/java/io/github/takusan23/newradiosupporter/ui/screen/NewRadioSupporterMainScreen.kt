package io.github.takusan23.newradiosupporter.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.takusan23.newradiosupporter.tool.PermissionCheckTool
import io.github.takusan23.newradiosupporter.tool.interop.PipViewModel
import io.github.takusan23.newradiosupporter.ui.theme.NewRadioSupporterTheme

/**
 * 下地になる画面
 * */
@Composable
fun NewRadioSupporterMainScreen() {
    NewRadioSupporterTheme {
        val context = LocalContext.current
        // ナビゲーション
        val navController = rememberNavController()
        // 権限なければ権限画面へ
        val startDestination =
            if (PermissionCheckTool.isGrantedPermission(context)) NavigationLinkList.HomeScreen else NavigationLinkList.PermissionScreen

        // ActivityにいるViewModelを使う
        val pipViewModel: PipViewModel = viewModel(context as ViewModelStoreOwner)
        val isPipModeFlow by pipViewModel.isInPipModeFlow.collectAsStateWithLifecycle()

        LaunchedEffect(navController, isPipModeFlow) {
            if (isPipModeFlow) {
                navController.navigate(NavigationLinkList.PipScreen)
            }
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable(NavigationLinkList.PermissionScreen) {
                PermissionScreen(onGranted = {
                    navController.navigate(NavigationLinkList.HomeScreen)
                })
            }
            composable(NavigationLinkList.HomeScreen) {
                HomeScreen { navController.navigate(it) }
            }
            composable(NavigationLinkList.SettingScreen) {
                SettingScreen(
                    onNavigate = { navController.navigate(it) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(NavigationLinkList.SettingLicenseScreen) {
                LicenseScreen { navController.popBackStack() }
            }
            composable(NavigationLinkList.PipScreen) {
                PipScreen(pipViewModel) {
                    navController.popBackStack()
                }
            }
        }
    }

}