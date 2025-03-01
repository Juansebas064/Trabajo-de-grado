package com.brightbox.hourglass

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.brightbox.hourglass.receivers.AppChangeReceiver
import com.brightbox.hourglass.view.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.view.Home
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {

    private val appChangeReceiver = AppChangeReceiver()
    private lateinit var appsViewModel: AppsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val homeViewModel: HomeViewModel by viewModels()
//        val appsViewModel: AppsViewModel by viewModels()
        appsViewModel = AppsViewModel.getInstance(this.applicationContext as Application)

        registerReceiver(
            appChangeReceiver,
            IntentFilter(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        )

        setContent {
            HourglassProductivityLauncherTheme() {
                Home(homeViewModel, appsViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

