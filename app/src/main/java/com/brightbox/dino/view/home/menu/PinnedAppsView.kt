package com.brightbox.dino.view.home.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.viewmodel.ApplicationsViewModel
import com.brightbox.dino.view.common.RoundedSquareButtonComponent
import com.brightbox.dino.view.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedAppsView(
    applicationsViewModel: ApplicationsViewModel = hiltViewModel(),
    sheetState: SheetState,
    modifier: Modifier
) {
    val apps by applicationsViewModel.appsList.collectAsState()
    val pinnedApps = apps.filter { app -> app.isPinned }
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(spacing.spaceExtraSmall)
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
                            applicationsViewModel.openApp(app)
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
                    text = context.getString(R.string.your_pinned_apps_go_here),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}