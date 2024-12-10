package com.brightbox.hourglass

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.brightbox.hourglass.receivers.AppUninstallReceiver
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



//        val intentFilter = IntentFilter(Intent.ACTION_PACKAGE_REMOVED).apply {
//            addDataScheme("package")
//        }
//        val receiver = AppUninstallReceiver()
//        registerReceiver(receiver, intentFilter)

        setContent {
            HourglassProductivityLauncherTheme() {
                Home(homeViewModel, appsViewModel)
            }
        }
    }

    private val uninstallAppLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // La aplicación fue desinstalada
            val packageName = result.data?.data?.encodedSchemeSpecificPart
            // Manejar la desinstalación aquí
        }
    }

    fun promptUninstallApp(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE).apply {
            data = Uri.parse("package:$packageName")
        }
        uninstallAppLauncher.launch(intent)
    }
}

