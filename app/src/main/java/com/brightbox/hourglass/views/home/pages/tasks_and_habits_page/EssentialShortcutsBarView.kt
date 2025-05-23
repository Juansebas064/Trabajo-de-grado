package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.constants.EssentialShortcutsEnum
import com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components.EssentialShortcutComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun EssentialShortcutsBarView(
    modifier: Modifier = Modifier,
    openApp: (Intent) -> Unit,
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
                    openApp(it.intent)
                },
            )
        }
    }
}