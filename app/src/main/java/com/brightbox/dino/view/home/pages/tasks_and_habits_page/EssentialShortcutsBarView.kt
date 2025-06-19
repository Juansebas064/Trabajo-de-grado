package com.brightbox.dino.view.home.pages.tasks_and_habits_page

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brightbox.dino.model.constants.EssentialShortcutsEnum
import com.brightbox.dino.navigation.ApplicationsLimitRoute
import com.brightbox.dino.navigation.LocalNavController
import com.brightbox.dino.view.home.pages.tasks_and_habits_page.components.EssentialShortcutComponent
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun EssentialShortcutsBarView(
    modifier: Modifier = Modifier,
    openApp: (Intent) -> Unit,
) {
    val spacing = LocalSpacing.current
    val navController = LocalNavController.current

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
        modifier = modifier
            .padding(horizontal = spacing.spaceSmall)
    ) {
        IconButton(
            modifier = Modifier,
            onClick = {
                navController.navigate(ApplicationsLimitRoute)
            }
        ) {
            Icon(
                imageVector = Icons.Default.TimerOff,
                contentDescription = "Limit apps",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        EssentialShortcutsEnum.entries.toTypedArray().forEach {
            EssentialShortcutComponent(
                essentialShortcutEnum = it,
                onClick = {
                    openApp(it.intent)
                },
            )
        }
    }
}