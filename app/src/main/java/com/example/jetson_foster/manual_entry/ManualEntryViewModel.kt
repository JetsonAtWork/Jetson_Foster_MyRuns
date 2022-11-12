package com.example.jetson_foster.manual_entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.jetson_foster.Services
import com.example.jetson_foster.room.ActiveEntry
import com.example.jetson_foster.room.EntryRepository
import com.example.jetson_foster.structs.EntryField
import com.example.jetson_foster.structs.InputType
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class ManualEntryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = EntryRepository.getInstance(application.applicationContext)
    private val _inputType = MutableStateFlow(-1)
    private val _activity = MutableStateFlow(-1)
    private val _date: MutableStateFlow<Calendar> by lazy { MutableStateFlow(Calendar.getInstance()) }
    private val _time: MutableStateFlow<Array<Int>> by lazy { MutableStateFlow(arrayOf(0,0,0)) }
    private val _duration = MutableStateFlow( 0.0.toLong() )
    private val _distance = MutableStateFlow(0.0)
    private val _calories = MutableStateFlow(0.0)
    private val _heartRate = MutableStateFlow(0.0)
    private val _comment = MutableStateFlow("")

    private fun setInput(newInput: Int) {
        _inputType.value = newInput
    }
    fun setActivity(newActivity: Int) {
        _activity.value = newActivity
    }
    fun setDate(year: Int, month: Int, day: Int) {
        _date.value.set(year, month, day)
    }
    fun setTime(hour: Int, minute: Int, second: Int) {
        _time.value = arrayOf(hour,minute,second)
    }
    fun setDuration(newDuration: Long) {
        _duration.value = newDuration
    }
    fun setDistance(newDistance: Double) {
        _distance.value = newDistance
    }
    fun setCalories(newCalories: Double) {
        _calories.value = newCalories
    }
    fun setHeartrate(newHeartRate: Double) {
        _heartRate.value = newHeartRate
    }
    fun setComment(newComment: String) {
        _comment.value = newComment
    }

    fun addDoubleDataByField(field: EntryField, data: Double) {
        when (field) {
            EntryField.DURATION -> setDuration(data.toLong())
            EntryField.DISTANCE -> setDistance(data)
            EntryField.CALORIES -> setCalories(data)
            EntryField.HEART_RATE -> setHeartrate(data)
            else -> return
        }
    }

    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            repo.clearAll()

        }
    }


   suspend fun saveEntry() {
        var distanceMult = 1.0
        if (Services.getDistanceUnits(getApplication<Application>().applicationContext) == "imperial") {
            distanceMult = 0.621371 //KM to Miles
        }
        val entry = ActiveEntry(
            0,
            InputType.MANUAL.asInt,
            _activity.value,
            _date.value,
            _duration.value,
            _distance.value*1000.0*distanceMult,
            0.0,
            0.0,
            _calories.value,
            0.0,
            _heartRate.value,
            _comment.value,
            ArrayList<LatLng>())

        withContext(Dispatchers.IO) {
            repo.insertEntry(entry)
        }
    }
}