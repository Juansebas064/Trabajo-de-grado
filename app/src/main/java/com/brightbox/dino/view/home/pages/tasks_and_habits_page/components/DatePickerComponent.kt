package com.brightbox.dino.view.home.pages.tasks_and_habits_page.components

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.utils.convertUTCMidnightToLocalMidnight
import com.brightbox.dino.utils.formatMillisecondsToSQLiteDate
import com.brightbox.dino.utils.getStartOfTodayMillisInUTC
import com.brightbox.dino.viewmodel.TimeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    modifier: Modifier = Modifier,
    label: String,
    date: Long? = null,
    setDate: (date: Long) -> Unit,
    enabled: Boolean = true,
    timeViewModel: TimeViewModel = hiltViewModel()
) {
    val today = timeViewModel.currentTimeMillis.collectAsState()

    val minDate: SelectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val startOfTodayMillis = getStartOfTodayMillisInUTC()
            Log.d("DatePickerComponent", "startOfTodayMillis: $startOfTodayMillis")

            return utcTimeMillis >= startOfTodayMillis
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (date == 0L) null else date,
        selectableDates = minDate
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        formatMillisecondsToSQLiteDate(convertUTCMidnightToLocalMidnight(it))
    } ?: ""

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        setDate(datePickerState.selectedDateMillis?.let { convertUTCMidnightToLocalMidnight(it) }
            ?: 0)
        Log.d("DatePickerComponent", "selectedDateMillis: ${datePickerState.selectedDateMillis}")
        coroutineScope.launch {
            delay(50)
            showDatePicker = false
        }
    }

    BackHandler(
        enabled = showDatePicker,
        onBack = {
            showDatePicker = false
        }
    )

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            enabled = enabled,
            label = {
                Text(
                    text = label,
                )
            },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                        // in the Initial pass to observe events before the text field consumes them
                        // in the Main pass.
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null && enabled) {
                            showDatePicker = true
                        }
                    }
                }
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        colors = DatePickerDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }
    }
}
