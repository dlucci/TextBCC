package com.dlucci.textbcc.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactNumber(
    val name : String,
    val phoneNumber : String
)
