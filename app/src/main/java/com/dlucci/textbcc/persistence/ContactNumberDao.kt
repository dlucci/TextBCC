package com.dlucci.textbcc.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactNumberDao {
    @Query("SELECT * FROM ContactGroupEntity")
    fun getAllGroups() : List<ContactGroupEntity>

    @Query("SELECT * FROM ContactGroupEntity WHERE groupName = :groupName")
    fun getGroup(groupName : String) : ContactGroupEntity

    @Insert
    fun insert(group : ContactGroupEntity)

    @Delete
    fun delete(group : ContactGroupEntity)
}