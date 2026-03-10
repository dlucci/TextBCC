package com.dlucci.textbcc.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dlucci.textbcc.model.ContactNumber

@Entity
data class ContactGroupEntity(
    @PrimaryKey val groupName : String,
    @ColumnInfo val contacts : List<ContactNumber>
)