package com.brightbox.hourglass.view

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.brightbox.hourglass.ui.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.viewmodel.AppsMenuViewModel

@Composable
fun AppList(viewModel: AppsMenuViewModel) {
    val apps by viewModel.appsList.observeAsState(emptyList())
    Box(
        modifier = Modifier
            .background(Color(0xFF131313))
            .padding(30.dp, 0.dp)
            .fillMaxSize()
    ) {
        LazyColumn (modifier = Modifier
            .clip(RoundedCornerShape(topStart = 8.dp))
            .clip(RoundedCornerShape(topEnd = 8.dp))
            .background(Color.Gray)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
        ) {
            items(apps) {app ->
                AppItem(app)
            }
        }
    }
}

@Composable
fun AppItem(app: ApplicationInfo) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = app.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, apiLevel = 29)
@Composable
fun GreetingPreview(appsMenuViewModel: AppsMenuViewModel) {
    HourglassProductivityLauncherTheme {
        AppList(appsMenuViewModel)
    }
}