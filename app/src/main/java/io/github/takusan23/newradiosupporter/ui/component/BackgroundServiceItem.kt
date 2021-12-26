package io.github.takusan23.newradiosupporter.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.newradiosupporter.R

/** サービス起動項目 */
@Composable
fun BackgroundServiceItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(10.dp),
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(),
        onClick = { onClick() }
    ) {
        CommonItem(
            icon = painterResource(id = R.drawable.ic_outline_mark_chat_unread_24),
            title = stringResource(id = R.string.service_state_button),
            description = stringResource(id = R.string.background_service_description),
        )
    }
}