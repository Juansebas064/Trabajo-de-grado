package com.brightbox.hourglass.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

// Format the milliseconds date to String according to SQLite date's best practices
fun formatMillisecondsToSQLiteDate(date: Long): String {
    val localDate = Instant.ofEpochMilli(date)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return localDate.format(formatter)
}

// Format the SQLite date to milliseconds
fun formatSQLiteDateToMilliseconds(date: String): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formattedDate = formatter.parse(date)
    return formattedDate?.time ?: 0L
}

// Format the milliseconds date to String according to SQLite date's best practices
fun formatMillisecondsToHours(date: Long): String {
    val localDate = Instant.ofEpochMilli(date)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localDate.format(formatter)
}