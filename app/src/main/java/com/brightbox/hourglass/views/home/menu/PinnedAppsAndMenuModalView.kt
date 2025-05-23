package com.brightbox.hourglass.views.home.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brightbox.hourglass.views.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedAppsAndMenuModalView(
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
        ) {
            PinnedAppsView(
                modifier = Modifier
                    .padding(horizontal = spacing.spaceSmall),
                sheetState = sheetState
            )
        }

        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                dragHandle = {

                },
                sheetState = sheetState,
                containerColor = Color.Transparent,
//                windowInsets = WindowInsets(0), // Para corregir el padding del handle
                modifier = Modifier.fillMaxHeight()
            ) {
                MenuView(
                    sheetState = sheetState
                )
            }
        }
    }
}