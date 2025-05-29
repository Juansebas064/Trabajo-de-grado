package com.brightbox.dino.utils

import android.content.Context
import android.util.Log
import com.brightbox.dino.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.round

private val SQLITE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")

// Get difference between two dates in days
fun getDifferenceInDays(startDate: String, endDate: String): Int {
    val startDateConverted = LocalDate.parse(startDate, SQLITE_DATE_FORMATTER)
    val endDateConverted = LocalDate.parse(endDate, SQLITE_DATE_FORMATTER)

    return ChronoUnit.DAYS.between(startDateConverted, endDateConverted).toInt()
}

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
        return "${round(((time/1000)/60).toDouble()).toInt()}"
    } else {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        val formattedDate = formatter.format(time)
        return formattedDate
    }
}

fun formatMillisecondsToDay(date: Long, context: Context? = null): String {
    val localDate = Instant.ofEpochMilli(date)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    var day = localDate.dayOfWeek.name.lowercase()

    if (context != null) {
        val packageName = context.packageName
        val resourceId = context.resources.getIdentifier(day, "string", packageName)
        day = context.getString(resourceId)
    }

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