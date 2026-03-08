package com.dlucci.textbcc.viewmodel

import androidx.lifecycle.ViewModel
import com.dlucci.textbcc.model.ContactNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class TextBccViewModel : ViewModel() {

    private val _selectedUsers = MutableStateFlow<List<ContactNumber>>(emptyList())
    val selectedUsers = _selectedUsers.asStateFlow()

    fun inputUsers(users : List<ContactNumber>) {
        _selectedUsers.value = users
    }

}