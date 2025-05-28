package com.brightbox.hourglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.brightbox.hourglass.views.preferences.PreferencesView
import com.brightbox.hourglass.views.theme.HourglassProductivityLauncherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HourglassProductivityLauncherTheme {
                PreferencesView()
            }
        }
    }
}