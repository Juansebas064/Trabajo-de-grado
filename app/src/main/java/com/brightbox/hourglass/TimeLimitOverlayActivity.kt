package com.brightbox.hourglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import com.brightbox.hourglass.views.common.BottomModalDialog
import com.brightbox.hourglass.views.theme.HourglassProductivityLauncherTheme
import com.brightbox.hourglass.views.theme.LocalSpacing
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimeLimitOverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val limitReachedPackageName = intent.getStringExtra("PACKAGE_NAME")

        setContent {
            HourglassProductivityLauncherTheme {
                val spacing = LocalSpacing.current
                val applicationsViewModel: ApplicationsViewModel = hiltViewModel()
                val state = applicationsViewModel.appsList.collectAsState()
                val application = state.value.find { it.packageName == limitReachedPackageName }

                if (application != null) {
                    BottomModalDialog(
                        onDismissRequest = {
                            finish()
                        }
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(spacing.spaceMedium)
                        ) {
                            Text(
                                text = "Overlay :D",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                            )
                            Text(
                                text = "Time limit exceeded for ${application!!.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}