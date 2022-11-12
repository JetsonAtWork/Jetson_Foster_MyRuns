package com.example.jetson_foster.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface EntryDao {
    @Query("SELECT * FROM entry")
    fun getAllEntries() : Flow<List<ActiveEntry>>

    @Query("SELECT * FROM entry e WHERE e.id = :targetId")
    fun getEntryById(targetId: Int) : Flow<ActiveEntry>

    @Insert()
    fun insertEntry(entry:ActiveEntry)

    @Query("DELETE FROM entry  WHERE id = :targetId")
    fun deleteEntry(targetId: Int)

}