package com.jeanloth.project.android.kotlin.axounaut.extensions
import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Pattern: yyyy-MM-dd HH:mm:ss
 */
fun Date.formatDateToOtherFormat(): String{
    val sdf= SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}


@SuppressLint("SimpleDateFormat")
fun LocalDate.formatDateToOtherFormat(initDateFormat: String, endDateFormat: String): String? {
    val initDate = SimpleDateFormat(initDateFormat).parse(this.toString())
    val formatter = SimpleDateFormat(endDateFormat)
    return formatter.format(initDate)
}

fun String.convertToLocalDate() = LocalDate.parse(this, DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRANCE))

fun Date.formatToTruncatedDateTime(): String{
    val sdf= SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: yyyy-MM-dd
 */
fun Date.formatToServerDateDefaults(): String{
    val sdf= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToServerTimeDefaults(): String{
    val sdf= SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: dd/MM/yyyy HH:mm:ss
 */
fun Date.formatToViewDateTimeDefaults(): String{
    val sdf= SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: dd/MM/yyyy
 */
fun LocalDate.formatToShortDate(): String{
    val sdf= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}

fun LocalDate.formatServerFormatToShortDate(): String{
    val originalFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
    val targetFormat = SimpleDateFormat("dd/MM/yyyy")
    val date: Date? = originalFormat.parse(this.toString())
    return targetFormat.format(date!!)
}

/**
 * Pattern: dd/MM/yyyy
 */
fun Calendar.formatToShortDate(): String{
    val sdf= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.toTimeSeconds(): String{
    val sdf= SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.toTime(): String{
    val sdf= SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Add field date to current date
 */
fun Date.add(field: Int, amount: Int): Date {
    Calendar.getInstance().apply {
        time = this@add
        add(field, amount)
        return time
    }
}

fun Date.addYears(years: Int): Date{
    return add(Calendar.YEAR, years)
}
fun Date.addMonths(months: Int): Date {
    return add(Calendar.MONTH, months)
}
fun Date.addDays(days: Int): Date{
    return add(Calendar.DAY_OF_MONTH, days)
}
fun Date.addHours(hours: Int): Date{
    return add(Calendar.HOUR_OF_DAY, hours)
}
fun Date.addMinutes(minutes: Int): Date{
    return add(Calendar.MINUTE, minutes)
}
fun Date.addSeconds(seconds: Int): Date{
    return add(Calendar.SECOND, seconds)
}
