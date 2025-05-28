package com.brightbox.hourglass.views.home.menu

import android.util.Log
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import com.brightbox.hourglass.views.common.RoundedSquareButtonComponent
import com.brightbox.hourglass.views.theme.LocalSpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedAppsView(
    applicationsViewModel: ApplicationsViewModel = hiltViewModel(),
    sheetState: SheetState,
    modifier: Modifier
) {
    val apps by applicationsViewModel.appsList.collectAsState()
    val pinnedApps = apps.filter { app -> app.isPinned }
    val coroutineScope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    Log.d("PinnedApps", "apps: $apps")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(spacing.spaceSmall)
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
        )

        if (pinnedApps.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                items(items = pinnedApps) { app ->
                    RoundedSquareButtonComponent(
                        text = app.name,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        padding = spacing.spaceSmall + spacing.spaceExtraSmall,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        containerColor = MaterialTheme.colorScheme.surface,
                        onClick = {
                            applicationsViewModel.openApp(app.packageName)
                        },
                        modifier = Modifier
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Your pinned apps go here",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}