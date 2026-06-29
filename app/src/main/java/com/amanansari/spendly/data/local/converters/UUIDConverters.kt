package com.amanansari.spendly.data.local.converters

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverters {

    @TypeConverter
    fun UUIDToString(uuid: UUID?) : String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun StringToUUID(value : String?) : UUID? {
        return value?.let { UUID.fromString(it) }
    }
}