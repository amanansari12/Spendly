package com.amanansari.spendly.data.local.converters

import androidx.room.TypeConverter
import com.amanansari.spendly.data.local.entity.TransactionType

class TransactionTypeConverter {

    @TypeConverter
    fun enumToType(type : TransactionType) : String = type.name

    @TypeConverter
    fun typeToEnum(value : String) : TransactionType = TransactionType.valueOf(value)

}