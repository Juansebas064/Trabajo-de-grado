package com.brightbox.hourglass.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.view.home.components.PinnedAppsComponent
import com.brightbox.hourglass.viewmodel.AppsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedAppsAndMenuModal(
    sheetState: SheetState,
    appsViewModel: AppsViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
        ) {
            PinnedAppsComponent(
                appsViewModel = appsViewModel,
                sheetState = sheetState,
                modifier = Modifier
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
                windowInsets = WindowInsets(0), // Para corregir el padding del handle
                modifier = Modifier.fillMaxHeight()
            ) {
                AppMenu(
                    appsViewModel = appsViewModel,
                    sheetState = sheetState
                )
            }
        }
    }
}