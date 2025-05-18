package com.brightbox.hourglass.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

// Format the milliseconds date to String according to SQLite date's best practices
fun formatMillisecondsToSQLiteDate(date: Long): String {
    val localDate = Instant.ofEpochMilli(date)
        .atZone(ZoneId.systemDefault())
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
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localDate.format(formatter)
}

fun formatMillisecondsToDay(date: Long): String {
    val localDate = Instant.ofEpochMilli(date)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return localDate.dayOfWeek.name.lowercase()
}

fun convertUTCMidnightToLocalMidnight(utcMidnightMillis: Long): Long {
    val utcInstant = Instant.ofEpochMilli(utcMidnightMillis)
    val utcDate = utcInstant.atZone(ZoneOffset.UTC).toLocalDate()
    val systemZone = ZoneId.systemDefault()
    val localStartOfDay = utcDate.atStartOfDay(systemZone)
    return localStartOfDay.toInstant().toEpochMilli()
}

fun getStartOfTodayMillisInUTC(): Long {
    val today = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    return today
}