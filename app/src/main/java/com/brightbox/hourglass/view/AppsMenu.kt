package com.brightbox.hourglass.view

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brightbox.hourglass.ui.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.viewmodel.AppsMenuViewModel

@Composable
fun AppList(viewModel: AppsMenuViewModel) {
    val apps by viewModel.appsList.observeAsState(emptyList())
    HourglassProductivityLauncherTheme {
        Box(
            modifier = Modifier
                .background(Color(0xFF131313))
                .padding(30.dp, 0.dp)
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                items(apps) { app ->
                    AppItem(app, viewModel)
                }
            }
        }
    }
}

@Composable
fun AppItem(app: Map<String, String>, viewModel: AppsMenuViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable {
                app["packageName"]?.let { viewModel.openApp(it) }
            }
    ) {
        app["appName"]?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 30.dp)

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