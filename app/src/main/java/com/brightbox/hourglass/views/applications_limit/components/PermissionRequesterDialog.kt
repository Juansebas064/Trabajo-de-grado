package com.brightbox.hourglass.views.applications_limit.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.brightbox.hourglass.views.common.BottomModalDialog
import com.brightbox.hourglass.views.common.RoundedSquareButtonComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun PermissionRequesterDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: (Boolean) -> Unit,
    isUsageAccessPermissionGranted: Boolean,
    isSystemAlertWindowPermissionGranted: Boolean,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    BottomModalDialog(
        onDismissRequest = {
            onDismissRequest(false)
        },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = spacing.spaceLarge,
                    bottom = spacing.spaceLarge,
                    start = spacing.spaceLarge,
                    end = spacing.spaceLarge
                )
        ) {
            Text(
                text = "Additional permissions required",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = "In order to use the application time limit feature, you need to grant some permissions required to get your app's usage time from your device and to show dialogs outside the launcher.",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Center
            )

            RoundedSquareButtonComponent(
                text = "Grant usage access",
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                containerColor =
                    if (!isUsageAccessPermissionGranted) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    if (!isUsageAccessPermissionGranted) {
                        val intent =
                            Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            RoundedSquareButtonComponent(
                text = "Grant system alert window access",
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                containerColor =
                    if (!isSystemAlertWindowPermissionGranted) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    if (!isSystemAlertWindowPermissionGranted) {
                        val intent =
                            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}