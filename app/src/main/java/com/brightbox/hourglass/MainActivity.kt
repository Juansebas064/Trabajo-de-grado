package com.brightbox.hourglass

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.brightbox.hourglass.view.home.HomeView
import com.brightbox.hourglass.view.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.viewmodel.AppsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val appChangeReceiver = AppChangeReceiver()
//    private val intentFilter = IntentFilter().apply {
//        addAction(Intent.ACTION_PACKAGE_REMOVED)
//        addAction(Intent.ACTION_PACKAGE_ADDED)
//        addDataScheme("package")
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        registerReceiver(
//            appChangeReceiver,
//            intentFilter
//        )

        setContent {
            HourglassProductivityLauncherTheme() {
                val appsViewModel: AppsViewModel by viewModels()
                HomeView(appsViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
//        registerReceiver(
//            appChangeReceiver,
//            intentFilter
//        )
    }

    override fun onPause() {
        super.onPause()
//        unregisterReceiver(appChangeReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(appChangeReceiver)
    }
}

