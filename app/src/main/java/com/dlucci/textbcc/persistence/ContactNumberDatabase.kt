package com.dlucci.textbcc.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dlucci.textbcc.model.Converters

@Database(entities = [ContactGroupEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ContactNumberDatabase : RoomDatabase() {
    abstract fun contactDao() : ContactNumberDao
}