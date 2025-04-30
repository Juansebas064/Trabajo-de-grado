package com.brightbox.hourglass

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.views.home.HomeView
import com.brightbox.hourglass.views.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import com.brightbox.hourglass.viewmodel.CategoriesViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val appChangeReceiver = ApplicationsChangeReceiver()
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

        val applicationsViewModel: ApplicationsViewModel by viewModels()
        val tasksViewModel: TasksViewModel by viewModels()
        val categoriesViewModel: CategoriesViewModel by viewModels()

        setContent {
            HourglassProductivityLauncherTheme() {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                HomeView(applicationsViewModel, tasksViewModel, categoriesViewModel)
                }
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

