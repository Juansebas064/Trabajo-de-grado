package com.brightbox.hourglass

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.brightbox.hourglass.receivers.AppUninstallReceiver
import com.brightbox.hourglass.view.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.view.Home
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {



//    private val appUninstallReceiver = AppUninstallReceiver()
    private lateinit var appsViewModel: AppsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val homeViewModel: HomeViewModel by viewModels()
//        val appsViewModel: AppsViewModel by viewModels()
        appsViewModel = AppsViewModel.getInstance(this.applicationContext as Application)

//        registerReceiver(
//            appUninstallReceiver,
//            IntentFilter(Intent.ACTION_PACKAGE_REMOVED)
//        )

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

