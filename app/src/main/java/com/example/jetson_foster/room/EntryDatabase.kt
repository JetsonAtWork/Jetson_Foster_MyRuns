package com.example.jetson_foster.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [ActiveEntry::class],
    version = 5)
abstract class EntryDatabase: RoomDatabase() {
    abstract fun dao(): EntryDao

    companion object {
        private var instance: EntryDatabase? = null
        fun getInstance(context: Context): EntryDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    EntryDatabase::class.java,
                    "entry.db")
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as EntryDatabase
        }
    }
}