package io.github.takusan23.newradiosupporter.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.takusan23.newradiosupporter.tool.NetworkStatusFlow
import io.github.takusan23.newradiosupporter.tool.interop.PipViewModel
import io.github.takusan23.newradiosupporter.ui.component.BandItem
import io.github.takusan23.newradiosupporter.ui.component.SimNetWorkStatusForPip

@Composable
fun PipScreen(viewModel: PipViewModel, onNavigate: () -> Unit = {}) {
    val isPipMode by viewModel.isInPipModeFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val multipleNetworkStatusDataList = NetworkStatusFlow.collectMultipleNetworkStatus(context)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val currentActiveSimSlot = remember {
        NetworkStatusFlow.getDataUsageSimSlotIndex(context)
    }
    LaunchedEffect(isPipMode) {
        if (!isPipMode) {
            onNavigate()
        }
    }
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            multipleNetworkStatusDataList.value.filter { statusData ->
                statusData.simSlotIndex == currentActiveSimSlot
            }.map { status ->
                Card(
                    modifier = Modifier
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            1.dp,
                        )
                    ),
                ) {
                    SimNetWorkStatusForPip(
                        modifier = Modifier.padding(
                            top = 2.dp,
                            start = 2.dp,
                            end = 2.dp
                        ),
                        finalNRType = status.finalNRType,
                    )
                    BandItem(
                        modifier = Modifier
                            .padding(
                                top = 5.dp,
                            )
                            .scale(0.75f),
                        bandData = status.bandData
                    )
                }
            }
        }
    }

}