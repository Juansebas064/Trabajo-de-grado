package com.brightbox.hourglass.view.home.pages.tasks_page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.view.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.AppsViewModel

@Composable
fun TasksPage(
    appsViewModel: AppsViewModel,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = spacing.spaceExtraLarge)
            ) {
                Text(
                    text = "21:27",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
        EssentialShortcutsBar(
            appsViewModel = appsViewModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}