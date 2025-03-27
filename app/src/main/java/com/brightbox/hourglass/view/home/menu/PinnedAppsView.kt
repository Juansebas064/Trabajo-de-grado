package com.brightbox.hourglass.view.home.menu

import android.util.Log
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.view.home.components.PilledTextButtonComponent
import com.brightbox.hourglass.viewmodel.AppsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedAppsView(
    appsViewModel: AppsViewModel,
    sheetState: SheetState,
    modifier: Modifier
) {
    val apps by appsViewModel.appsList.collectAsState()
    val pinnedApps = apps.filter { app -> app.isPinned }
    val coroutineScope = rememberCoroutineScope()

    Log.d("PinnedApps", "apps: $apps")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(100.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -10) { // Detect upward swipe
                        coroutineScope.launch {
                            sheetState.expand()
                        }
                        Log.d("PinnedApps", "Upward swipe detected")
                    }
                }
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(0.dp)
        ) {
            items(items = pinnedApps) { app ->
                PilledTextButtonComponent(
                    text = app.name,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    onClick = {
                        appsViewModel.openApp(app.packageName)
                    },
                )
            }
        }
    }
}