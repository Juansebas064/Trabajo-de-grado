package com.brightbox.dino

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.viewmodel.ApplicationsViewModel
import com.brightbox.dino.view.common.BottomModalDialogComponent
import com.brightbox.dino.view.common.RoundedSquareButtonComponent
import com.brightbox.dino.view.theme.DinoLauncherTheme
import com.brightbox.dino.view.theme.LocalSpacing
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimeLimitOverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val limitReachedPackageName = intent.getStringExtra("PACKAGE_NAME")

        setContent {
            DinoLauncherTheme {
                val spacing = LocalSpacing.current
                val applicationsViewModel: ApplicationsViewModel = hiltViewModel()
                val state = applicationsViewModel.appsList.collectAsState()
                val application = state.value.find { it.packageName == limitReachedPackageName }
                val context = LocalContext.current

                if (application != null) {
                    BottomModalDialogComponent(
                        onDismissRequest = {
                            finish()
                        }
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(spacing.spaceLarge)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "${context.getString(R.string.time_limit_exceeded)} ${application.name}",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(Modifier.height(spacing.spaceMedium))

                            Text(
                                text = context.getString(R.string.consider_to_rest_or_do_your_tasks),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                lineHeight = MaterialTheme.typography.bodyLarge.fontSize
                            )

                            Spacer(Modifier.height(spacing.spaceMedium))

                            RoundedSquareButtonComponent(
                                text = context.getString(R.string.exit),
                                textStyle = MaterialTheme.typography.bodyMedium,
                                onClick = {
                                    finish()
                                    val launcherActivity = Intent(Intent.ACTION_MAIN).apply {
                                        addCategory(Intent.CATEGORY_HOME)
                                        addFlags(FLAG_ACTIVITY_NEW_TASK)
                                        addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                                    }
                                    context.startActivity(launcherActivity)
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}