package com.brightbox.hourglass

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
import com.brightbox.hourglass.view.home.Home
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {

    private val appChangeReceiver = AppChangeReceiver()
    private val intentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_PACKAGE_REMOVED)
        addAction(Intent.ACTION_PACKAGE_ADDED)
        addDataScheme("package")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val homeViewModel: HomeViewModel by viewModels()
        val appsViewModel: AppsViewModel by viewModels()

        registerReceiver(
            appChangeReceiver,
            intentFilter
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
        registerReceiver(
            appChangeReceiver,
            intentFilter
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(appChangeReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(appChangeReceiver)
    }
}

