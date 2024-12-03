package com.brightbox.hourglass.view

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brightbox.hourglass.model.ApplicationModel
import com.brightbox.hourglass.viewmodel.AppsViewModel

@Composable
fun AppMenu(viewModel: AppsViewModel) {

    // States
    val apps by viewModel.appsList.collectAsState()
    var searchText by remember { mutableStateOf("") }

    // Control functions
    val getAppsLambda: () -> Unit = { viewModel.getApps() }
    val openAppLambda: (String) -> Unit = { viewModel.openApp(it) }
    val onSearchTextChangeLambda: (String) -> Unit = {
        searchText = it
        viewModel.getApps(it)
    }
    val onEnterPressedWhenSearching: () -> Unit = {
        viewModel.openApp(apps[0].packageName)
    }

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
        FiltersBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .weight(0.2f)
        )
        // App list
        AppColumnList(
            apps = apps,
            getApps = getAppsLambda,
            openApp = openAppLambda,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .fillMaxWidth()
                .weight(1.5f)
                .padding(top = 15.dp, start = 15.dp, end = 15.dp)
        )
        // Search bar
        SearchBar(
            searchText = searchText,
            onSearchTextChange = onSearchTextChangeLambda,
            onEnterPressedWhenSearching = onEnterPressedWhenSearching,
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth(0.9f)
                .imePadding()
        )
    }
}

@Composable
fun FiltersBox(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun AppColumnList(
    apps: List<ApplicationModel>,
    getApps: () -> Unit,
    openApp: (String) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier,
    ) {
        items(apps) { app ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        openApp(app.packageName)
                        getApps()
                    }
            ) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
            }

        }
    }
}

@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onEnterPressedWhenSearching: () -> Unit,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { onSearchTextChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onEnterPressedWhenSearching()
                }
            ),
            textStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterStart)
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
                .focusable(true)
        )
    }
}


