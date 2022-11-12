package com.example.jetson_foster.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class EntryRepository(
    private val db: EntryDatabase
) {
    //val entries: Flow<List<ActiveEntry>> = getAllEntries()

    fun insertEntry(entry:ActiveEntry) {
        db.dao().insertEntry(entry)
    }

    fun clearAll() {
        db.clearAllTables()
    }
    fun getEntryById(id: Int) : Flow<ActiveEntry?> {
        return db.dao().getEntryById(id)
    }

    fun deleteEntry(targetId: Int) {
        db.dao().deleteEntry(targetId)
    }

    fun getAllEntries() : Flow<List<ActiveEntry>>{
        return db.dao().getAllEntries()
    }

    companion object {
        private var instance: EntryRepository? = null
        fun getInstance(context: Context) : EntryRepository {
            if (instance == null) {
                instance = EntryRepository(EntryDatabase.getInstance(context))
            }
            return instance as EntryRepository
        }
    }
}