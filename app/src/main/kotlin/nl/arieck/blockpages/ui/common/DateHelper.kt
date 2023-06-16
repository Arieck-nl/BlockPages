package nl.arieck.blockpages.ui.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
fun String?.toMonthDate(): String? {
    this ?: return null
    return try {
        val fromDtf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        fromDtf.timeZone = TimeZone.getTimeZone("UTC")
        val toDtf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        fromDtf.parse(this)?.run { toDtf.format(this) }
    } catch (e: ParseException) {
        this
    }
}