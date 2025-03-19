package com.brightbox.hourglass.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.viewmodel.AppsViewModel

@Composable
fun PinnedApps(
    appsViewModel: AppsViewModel,
    modifier: Modifier
) {
    val apps by appsViewModel.appsList.collectAsState()
    Column(
        modifier = modifier
    ) {
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Icon(
//                imageVector = Icons.Default.KeyboardArrowUp,
//                contentDescription = "",
//                tint = MaterialTheme.colorScheme.onBackground
//            )
//        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            items(items = apps.applications.filter { app -> app.isPinned }) { app ->
                TextButton(
                    onClick = {
                        appsViewModel.openApp(app.packageName)
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}