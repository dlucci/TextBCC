package com.dlucci.textbcc.module

import androidx.room.Room
import com.dlucci.textbcc.persistence.ContactNumberDatabase
import com.dlucci.textbcc.viewmodel.TextBccViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TextBccViewModel() }
    single { Room.databaseBuilder(androidContext(), ContactNumberDatabase::class.java, "contact-groups" ).build() }

}