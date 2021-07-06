package com.jeanloth.project.android.kotlin.local.converters

import io.objectbox.converter.PropertyConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class DateConverter : PropertyConverter<LocalDate?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): LocalDate? {
        if (databaseValue == null) {
            return LocalDate.now()
        }
        return LocalDate.parse(databaseValue)
    }

    override fun convertToDatabaseValue(entityProperty: LocalDate?): String? {
        return entityProperty?.formatToServerDateDefaults() ?: LocalDate.now().formatToServerDateDefaults()
    }
}

/**
 * Pattern: yyyy-MM-dd
 */
fun LocalDate.formatToServerDateDefaults(): String{
    val sdf= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}