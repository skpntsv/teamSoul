package ru.nsu.teamsoul.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveLayout(
    modifier: Modifier = Modifier,
    mobileContent: @Composable () -> Unit,
    desktopContent: @Composable () -> Unit
) {
    BoxWithConstraints(modifier) {
        if (maxWidth < 840.dp) {
            mobileContent()
        } else {
            desktopContent()
        }
    }
}