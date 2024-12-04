package com.brightbox.hourglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.brightbox.hourglass.view.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.view.Home
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val homeViewModel: HomeViewModel by viewModels()
        val appsViewModel: AppsViewModel by viewModels()
        setContent {
            HourglassProductivityLauncherTheme() {
                Home(homeViewModel, appsViewModel)
            }
        }
    }

//    override fun onBackPressed() {
//
//    }
}

