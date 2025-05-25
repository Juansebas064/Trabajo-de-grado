package com.brightbox.hourglass.views.home.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedAppsAndMenuModalView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
    ) {
        PinnedAppsView(
            modifier = Modifier
                .padding(horizontal = spacing.spaceSmall),
            sheetState = sheetState
        )

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