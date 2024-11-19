package com.brightbox.hourglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.ui.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.view.AppList
import com.brightbox.hourglass.viewmodel.AppsMenuViewModel

class MainActivity(private val viewModel: AppsMenuViewModel) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HourglassProductivityLauncherTheme {
                AppList(viewModel)
            }
        }
    }
}

