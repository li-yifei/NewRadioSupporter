package io.github.takusan23.newradiosupporter.ui.screen

import android.graphics.Rect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.takusan23.newradiosupporter.BackgroundNrSupporter
import io.github.takusan23.newradiosupporter.R
import io.github.takusan23.newradiosupporter.tool.NetworkStatusFlow
import io.github.takusan23.newradiosupporter.tool.PermissionCheckTool
import io.github.takusan23.newradiosupporter.tool.SettingIntentTool
import io.github.takusan23.newradiosupporter.tool.interop.PipViewModel
import io.github.takusan23.newradiosupporter.ui.component.*

/** 回線状態を表示している Card の tonalElevation */
private val CardTonalElevation = 1.dp

/**
 * ホーム画面
 *
 * @param onNavigate 画面遷移を行う際に呼ばれる
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    // スクロールしたら AppBar を小さくするやつ
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    // Flowで収集する
    val isUnlimitedNetwork = NetworkStatusFlow.collectUnlimitedNetwork(context)
        .collectAsStateWithLifecycle(initialValue = null)
    val multipleNetworkStatusDataList = NetworkStatusFlow.collectMultipleNetworkStatus(context)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    // バックグラウンドの権限ダイアログを出すか
    val isOpenBackgroundPermissionDialog = remember { mutableStateOf(false) }
    val pipViewModel: PipViewModel = viewModel(context as ViewModelStoreOwner)
    val isPipMode by pipViewModel.isInPipModeFlow.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        if (!isPipMode) {
            MediumTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = { AboutMenuIcon { onNavigate(NavigationLinkList.SettingScreen) } },
                scrollBehavior = scrollBehavior
            )
        }
    }) {
        Box(modifier = Modifier.padding(it)) {

            // バックグラウンド 5G 通知機能の権限ダイアログ
            if (isOpenBackgroundPermissionDialog.value) {
                BackgroundNrPermissionDialog(onDismissRequest = {
                    isOpenBackgroundPermissionDialog.value = false
                }, onGranted = { BackgroundNrSupporter.toggleService(context) })
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // SIM カードの枚数分表示
                items(multipleNetworkStatusDataList.value) { status ->
                    // 押したら展開できるようにするため
                    // 初期値はデータ通信に設定されたSIMカードのスロット番号
                    val isExpanded = remember {
                        mutableStateOf(
                            status.simSlotIndex == NetworkStatusFlow.getDataUsageSimSlotIndex(
                                context
                            )
                        )
                    }

                    Card(modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .onGloballyPositioned { layoutCoordinates ->
                            if (isExpanded.value) {
                                val boundsInWindow = layoutCoordinates.boundsInWindow()
                                pipViewModel.cardRect.value = Rect(
                                    boundsInWindow.left.toInt(),
                                    boundsInWindow.top.toInt(),
                                    boundsInWindow.right.toInt(),
                                    boundsInWindow.bottom.toInt()
                                )
                            }
                        }, colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            CardTonalElevation
                        )
                    ), onClick = { isExpanded.value = !isExpanded.value }) {
                        // 押したら展開できるように
                        if (isExpanded.value) {
                            SimNetWorkStatusExpanded(
                                modifier = Modifier.padding(
                                    top = 10.dp, start = 10.dp, end = 10.dp
                                ),
                                finalNRType = status.finalNRType,
                                nrStandAloneType = status.nrStandAloneType
                            )
                            BandItem(
                                modifier = Modifier.padding(
                                    top = 10.dp, start = 10.dp, end = 10.dp
                                ), bandData = status.bandData
                            )
                        } else {
                            SimNetworkOverview(
                                simIndex = status.simSlotIndex + 1,
                                bandData = status.bandData,
                                finalNRType = status.finalNRType,
                                nrStandAloneType = status.nrStandAloneType,
                            )
                        }
                    }

                }
                item {
                    if (isUnlimitedNetwork.value != null) {
                        UnlimitedInfo(
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
                            isUnlimited = isUnlimitedNetwork.value!!
                        )
                    }
                }
                item {
                    OpenMobileNetworkSettingMenu(modifier = Modifier.padding(
                        top = 10.dp, start = 10.dp, end = 10.dp
                    ), onClick = { SettingIntentTool.openMobileDataNetworkSetting(context) })
                }
                item {
                    BackgroundServiceItem(modifier = Modifier.padding(
                        top = 10.dp, start = 10.dp, end = 10.dp
                    ), onClick = {
                        // 権限があれば起動
                        if (PermissionCheckTool.isGrantedNotificationPermission(context) && PermissionCheckTool.isGrantedBackgroundLocationPermission(
                                context
                            )
                        ) {
                            BackgroundNrSupporter.toggleService(context)
                        } else {
                            isOpenBackgroundPermissionDialog.value = true
                        }
                    })
                }
            }
        }
    }
}