package com.dlucci.textbcc.module

import com.dlucci.textbcc.viewmodel.TextBccViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TextBccViewModel() }
}