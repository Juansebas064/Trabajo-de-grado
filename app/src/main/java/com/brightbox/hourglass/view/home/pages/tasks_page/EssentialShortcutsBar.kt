package com.brightbox.hourglass.view.home.pages.tasks_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.model.EssentialShortcutsEnum
import com.brightbox.hourglass.view.home.components.EssentialShortcutComponent
import com.brightbox.hourglass.view.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.AppsViewModel

@Composable
fun EssentialShortcutsBar(
    appsViewModel: AppsViewModel,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .padding(spacing.spaceSmall)
    ) {
        EssentialShortcutsEnum.entries.toTypedArray().forEach {
            EssentialShortcutComponent(
                essentialShortcutEnum = it,
                onClick = {
                    appsViewModel.openApp(it.intent)
                },
            )
        }
    }
}