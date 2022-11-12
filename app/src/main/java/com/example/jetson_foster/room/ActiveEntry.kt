package com.example.jetson_foster.room

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetson_foster.structs.ActivityType
import com.example.jetson_foster.structs.InputType
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.roundToInt

//TODO: Maybe do NonNull
@Entity(tableName = "entry")
data class ActiveEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val inputType: Int,
    val activity: Int,
    val dateTime: Calendar,
    val duration: Long,
    val distance: Double,
    val avgPace: Double,
    val avgSpeed: Double,
    val calories: Double,
    val climb: Double,
    val heartRate: Double,
    val comment: String,
    val locationList: List<LatLng>?
) {
    fun inputString(): String {return InputType.fromInt(inputType).toString()}
    fun durationString(): String {
        val min = TimeUnit.SECONDS.toMinutes(duration)
        //val sec = TimeUnit.MILLISECONDS.toSeconds(duration)
        val sec = 0
        return "$min mins $sec secs "
    }
    fun activityString() = ActivityType.stringFromInt(activity)
    fun dateTimeString(): String = Companion.format.format(dateTime.time)
    fun distanceString(unit: String): String {
        return if (unit == "imperial") {
            "${roundToTwoDec(distance * 0.000621371)} Miles"
        } else {
            "${roundToTwoDec(distance/1000)} Kilometres"
        }
    }
    fun caloriesString(): String {
        return "$calories cals"
    }
    fun heartRateString(): String {
        return "$heartRate bpm"
    }

    override fun toString(): String {
        return "ActiveEntry(id=$id, \ninputType=$inputType,\n activity=$activity,\n dateTime=$dateTime,\n duration=$duration,\n distance=$distance,\n avgPace=$avgPace,\n avgSpeed=$avgSpeed,\n calories=$calories,\n climb=$climb,\n heartRate=$heartRate,\n comment='$comment',\n locationList=$locationList)"
    }


    companion object {
        @SuppressLint("SimpleDateFormat")
        private val format = SimpleDateFormat("hh:mm:ss MMM d yyyy")
        private fun roundToTwoDec(double: Double): Double {
            return (double* 100.0).roundToInt()/100.0
        }
    }
}