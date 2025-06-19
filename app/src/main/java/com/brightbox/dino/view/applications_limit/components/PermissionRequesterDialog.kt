package com.brightbox.dino.view.applications_limit.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.brightbox.dino.R
import com.brightbox.dino.view.common.BottomModalDialogComponent
import com.brightbox.dino.view.common.RoundedSquareButtonComponent
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun PermissionRequesterDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: (Boolean) -> Unit,
    isUsageAccessPermissionGranted: Boolean,
    isSystemAlertWindowPermissionGranted: Boolean,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    BottomModalDialogComponent(
        onDismissRequest = {
            onDismissRequest(false)
        },
    ) {
        Column(
//            verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
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
                text = context.getString(R.string.additional_permissions_required),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(spacing.spaceMedium))

            Text(
                text = context.getString(R.string.in_order_to_use_app_limit),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(spacing.spaceMedium))

            RoundedSquareButtonComponent(
                text = context.getString(R.string.grant_usage_access),
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

            Spacer(Modifier.height(spacing.spaceSmall))

            RoundedSquareButtonComponent(
                text = context.getString(R.string.grant_system_alert_window),
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