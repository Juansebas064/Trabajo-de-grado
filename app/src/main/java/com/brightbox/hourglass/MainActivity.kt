package com.brightbox.hourglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.brightbox.hourglass.ui.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.view.AppList
import com.brightbox.hourglass.viewmodel.AppsMenuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appsMenuViewModel: AppsMenuViewModel by viewModels()
        setContent {
            HourglassProductivityLauncherTheme {
                AppList(appsMenuViewModel)
            }
        }
    }
}

