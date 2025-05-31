package com.brightbox.dino.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import kotlin.math.round

private val SQLITE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun getDifferenceInDays(startDate: Long, endDate: String): Int {
    val startDateConverted = Instant.ofEpochMilli(startDate)
        .atZone(ZoneId.systemDefault()) // Considera la zona horaria del sistema
        .toLocalDate()
    val endDateConverted = LocalDate.parse(endDate, SQLITE_DATE_FORMATTER)
    val daysDifference = ChronoUnit.DAYS.between(startDateConverted, endDateConverted).toInt()

    return daysDifference
}

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
fun formatMillisecondsToMinutes(time: Long, showSeconds: Boolean = false): String {
    if (!showSeconds) {
        return "${round(((time / 1000) / 60).toDouble()).toInt()}"
    } else {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        val formattedDate = formatter.format(time)
        return formattedDate
    }
}

fun formatSQLiteDateToMonth(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formattedDate = formatter.parse(date)
    val calendar = Calendar.getInstance()
    calendar.time = formattedDate!!
    val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
    return month!!.lowercase()
}

fun formatMillisecondsToDay(date: Long): String {
    val localDate = Instant.ofEpochMilli(date)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    var day = localDate.dayOfWeek.name.lowercase()

    return day
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