package com.dlucci.textbcc.model

import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ContactNumber(
    val name : String,
    val phoneNumber : String
)

class Converters {
    @TypeConverter
    fun fromContactList(contacts : List<ContactNumber>) = Json.encodeToString(contacts)

    @TypeConverter
    fun fromString(json : String) = Json.decodeFromString<List<ContactNumber>>(json)
}