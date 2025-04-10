package com.brightbox.hourglass.views.home.pages.tasks_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.constants.EssentialShortcutsEnum
import com.brightbox.hourglass.views.home.pages.tasks_page.components.EssentialShortcutComponent
import com.brightbox.hourglass.views.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel

@Composable
fun EssentialShortcutsBarView(
    applicationsViewModel: ApplicationsViewModel,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
        modifier = modifier
            .padding(horizontal = spacing.spaceSmall)
    ) {
        EssentialShortcutsEnum.entries.toTypedArray().forEach {
            EssentialShortcutComponent(
                essentialShortcutEnum = it,
                onClick = {
                    applicationsViewModel.openApp(it.intent)
                },
            )
        }
    }
}