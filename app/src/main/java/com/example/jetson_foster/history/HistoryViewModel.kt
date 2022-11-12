package com.example.jetson_foster.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.Preference.OnPreferenceChangeListener
import com.example.jetson_foster.room.ActiveEntry
import com.example.jetson_foster.room.EntryRepository
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: EntryRepository = EntryRepository.getInstance(application.applicationContext)
    val entryList : Flow<List<ActiveEntry>> = repo.getAllEntries()
}