package com.brightbox.hourglass.view

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brightbox.hourglass.ui.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.viewmodel.AppsMenuViewModel

@Composable
fun AppList(viewModel: AppsMenuViewModel) {
    viewModel.getApps()
    val apps by viewModel.appsList.observeAsState(emptyList())
    var searchText by remember { mutableStateOf("") }
    HourglassProductivityLauncherTheme {
        // Parent container
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            // Menu text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "Menu",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            // Filters area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .weight(0.2f)
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            // App list
            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .fillMaxWidth()
                    .weight(1.5f)
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                items(apps) { app ->
                    AppItem(app, viewModel)
                }
            }
            // Search bar
            Box(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth(0.9f)
                    .imePadding()
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        if (searchText.isNotEmpty()) {
                            viewModel.searchApps(it)
                        } else {
                            viewModel.getApps()
                        }
                    },
                    textStyle = TextStyle.Default.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    ),
                    cursorBrush = SolidColor(Color.Unspecified),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                            if (searchText.isEmpty()) {
                                Text(
                                    text = "Search",
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            innerTextField()
                        }

                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .height(50.dp)
                        .focusable(false)
                )
            }
        }
    }
}

@Composable
fun AppItem(app: Map<String, String>, viewModel: AppsMenuViewModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                app["packageName"]?.let { viewModel.openApp(it) }
            }
    ) {
        app["appName"]?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )
        }
    }
}


//@Preview(showBackground = true, apiLevel = 29)
//@Composable
//fun GreetingPreview() {
//    val appsMenuViewModel: AppsMenuViewModel by viewModels()
//    val apps by viewModel.appsList.observeAsState(emptyList())
//    HourglassProductivityLauncherTheme {
//        AppList(appsMenuViewModel)
//    }
//}