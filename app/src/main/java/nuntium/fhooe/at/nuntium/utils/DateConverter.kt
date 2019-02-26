package nuntium.fhooe.at.nuntium.utils

import android.arch.persistence.room.TypeConverter
import java.util.*


class DateConverter {
        @TypeConverter
        fun toDate(dateLong: Long): Date? = Date(dateLong)

        @TypeConverter
        fun toLong(date: Date): Long = date.time
}