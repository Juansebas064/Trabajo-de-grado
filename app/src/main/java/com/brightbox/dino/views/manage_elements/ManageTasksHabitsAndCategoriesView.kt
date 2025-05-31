package com.brightbox.dino.views.manage_elements

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.brightbox.dino.views.manage_elements.components.NavigationBarComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun ManageTasksHabitsAndCategoriesView(
    modifier: Modifier = Modifier,
) {
    val activity = LocalActivity.current
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                when (selectedIndex) {
                    0 -> ManageTasksView(
                        modifier = Modifier.padding(
                            top = spacing.spaceLarge,
                            start = spacing.spaceLarge,
                            end = spacing.spaceLarge
                        )
                    )

                    1 -> ManageHabitsView(
                        modifier = Modifier.padding(
                            top = spacing.spaceLarge,
                            start = spacing.spaceLarge,
                            end = spacing.spaceLarge
                        )
                    )

                    2 -> ManageCategoriesView(
                        modifier = Modifier.padding(
                            top = spacing.spaceLarge,
                            start = spacing.spaceLarge,
                            end = spacing.spaceLarge
                        )
                    )
                }
            }

            NavigationBarComponent(
                selectedIndex = selectedIndex,
                onButtonClick = {
                    selectedIndex = it
                }
            )
        }

        // Close
        IconButton(
            onClick = {
                activity!!.finish()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}