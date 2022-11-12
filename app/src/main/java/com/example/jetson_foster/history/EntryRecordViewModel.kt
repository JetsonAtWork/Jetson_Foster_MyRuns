package com.example.jetson_foster.history
import android.app.Application
import android.icu.text.AlphabeticIndex.Record
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.jetson_foster.R
import com.example.jetson_foster.room.ActiveEntry
import com.example.jetson_foster.room.EntryRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EntryRecordViewModel(
    application: Application,
    val id: Int
    ) : AndroidViewModel(application) {
    private val repo = EntryRepository.getInstance(application.applicationContext)
    init {
        val prefs = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
        val unit = prefs.getString(application.applicationContext.getString(R.string.unit_system),"metric")
        viewModelScope.launch {
            repo.getEntryById(id).collect { record ->
                record?.let {
                    _uiState.value = RecordUiState.fromEntry(record,unit!!)
                }
            }
        }
    }

    private val _uiState: MutableStateFlow<RecordUiState> = MutableStateFlow(RecordUiState.emptyState())
    val uiState: StateFlow<RecordUiState>  = _uiState

    suspend fun deleteEntry(targetId: Int) {
        withContext(Dispatchers.IO) {
            repo.deleteEntry(targetId)

        }
    }


    class RecordUiState(
        val inputStr: String,
        val activityStr: String,
        val dateTimeStr: String,
        val durationStr: String,
        val distanceStr: String,
        val calories: String,
        val heartRate: String,
        val comment: String
    ) {
        companion object {

            fun fromEntry(entry: ActiveEntry, unit: String): RecordUiState {
                    return RecordUiState(
                        entry.inputString(),
                        entry.activityString(),
                        entry.dateTimeString(),
                        entry.durationString(),
                        entry.distanceString(unit),
                        entry.caloriesString(),
                        entry.heartRateString(),
                        entry.comment
                    )
            }
            fun emptyState(): RecordUiState {
                return RecordUiState(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
                )
            }
        }
    }
}