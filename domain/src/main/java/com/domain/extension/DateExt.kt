package com.domain.extension

import android.text.format.DateUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*


enum class DateFormatType(val value: String) {
    hh_mm_a("hh:mm a"),
    hh_mm_ss_a("hh:mm:ss a"),
    dd_MM_hh_mm_a("dd'/'MM',' hh:mm a"),
    dd_MM_yyyy("dd-MM-yyyy"),
    yyyy_MM_dd_T_HH_mm_ss_SSS_Z("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
}

private val hh_mm_a: SimpleDateFormat = SimpleDateFormat("hh:mm a")

fun Date?.getMessageTime(): CharSequence? {
    return hh_mm_a.format(this)
}

fun DateTime?.getMessageTime(): CharSequence? {
    val forPattern = DateTimeFormat.forPattern(DateFormatType.hh_mm_a.value)
    return forPattern.print(this)

}

fun DateTime?.getCopyMessageTime(): CharSequence? {
    val forPattern = DateTimeFormat.forPattern(DateFormatType.dd_MM_hh_mm_a.value)
    return forPattern.print(this)

}

fun DateTime?.getMessageDate(): CharSequence? {
    return DateTimeFormat.mediumDate().print(this)
}

fun DateTime?.isToday(): Boolean {
    if (this == null) {
        return false
    } else {
        return DateUtils.isToday(this.millis)
    }
}

fun String.parseDate(currentFormatType: DateFormatType, outputFormatType: DateFormatType): String {
    val parse = DateTime.parse(this, DateTimeFormat.forPattern(currentFormatType.value))
    val formatter = DateTimeFormat.forPattern(outputFormatType.value)
    return formatter.print(parse)
}

fun String.parseDate(currentFormatType: DateFormatType): DateTime {
    val parse = DateTime.parse(this, DateTimeFormat.forPattern(currentFormatType.value))
    return parse
}

fun String.parseDate(currentFormatType: DateTimeFormatter): DateTime {
    val parse = DateTime.parse(this, currentFormatType)
    return parse
}


fun DateTime.parseDate(outputFormatType: DateFormatType): String {
    val formatter = DateTimeFormat.forPattern(outputFormatType.value)
    return formatter.print(this)
}

fun DateTime.convertDefaultToUTC(): DateTime {
    return this.withZone(DateTimeZone.UTC)
}

fun DateTime.convertUTCToDefault(): DateTime {
    return this.withZone(DateTimeZone.getDefault())
}

fun Date.convertDefaultToUTC(): Date {
    val dateTime = DateTime(this, DateTimeZone.getDefault())
    val withZone = dateTime.withZone(DateTimeZone.UTC).toLocalDateTime()
    return withZone.toDate()
}

fun Date.convertUTCToDefault(): Date {
    val dateTime = DateTime(this, DateTimeZone.UTC)
    val withZone = dateTime.withZone(DateTimeZone.getDefault()).toLocalDateTime()
    return withZone.toDate()
}