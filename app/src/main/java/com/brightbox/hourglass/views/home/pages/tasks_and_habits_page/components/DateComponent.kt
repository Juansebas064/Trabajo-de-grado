package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.brightbox.hourglass.views.theme.LocalSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateComponent(
    currentTimeMillis: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    val date = Date(currentTimeMillis)
    val dateFormat = SimpleDateFormat("dd 〡 MM 〡 yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date)

    Row(
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
    }
}