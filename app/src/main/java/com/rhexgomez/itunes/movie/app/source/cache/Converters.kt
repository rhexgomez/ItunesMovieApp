package com.rhexgomez.itunes.movie.app.source.cache

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

/**
 * Used by the room database internally for converting [Date] to [Long] (or vice versa)!
 */
class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?) = timestamp?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?) = date?.time

}

/**
 * Used by the room database internally for converting [Uri] to [String] (or vice versa)!
 */
class UriConverter {

    @TypeConverter
    fun toUri(uri: String?) = uri?.let { Uri.parse(it) }

    @TypeConverter
    fun toTimestamp(uri: Uri?) = uri?.toString()
}
