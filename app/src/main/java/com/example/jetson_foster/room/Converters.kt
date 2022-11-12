package com.example.jetson_foster.room

import android.util.Log
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.util.*

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Calendar? = value?.let { value ->
        Calendar.getInstance().also { calendar ->
            calendar.timeInMillis = value
        }
    }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis


    private fun fromLatLng(latLng: LatLng?) : String? {
        if (latLng == null)
            return null
        return "(${latLng.latitude},${latLng.longitude}"
    }


    private fun toLatLng(string: String?) : LatLng?{
        if (string == null)
            return null
        Log.i("JETBUG","$string\n\n")
        val s = string.replace("(", "")?.replace(")", "")
        val list = s!!.split(",")
        return  LatLng(list.first().toDouble(), list.last().toDouble())
    }

    @TypeConverter
    @JvmStatic
    fun fromLatLngList(latLngList: List<LatLng>?): String? {
        if (latLngList == null)
            return null
        return latLngList.map { fromLatLng(it) }.joinToString { it!! }
    }

    @TypeConverter
    @JvmStatic
    fun toLatLngList(string: String?): List<LatLng?>? {
        if (string == null || string == "")
            return null

        val stringList = string.split(',')
        return stringList.map { toLatLng(it) }
    }
}