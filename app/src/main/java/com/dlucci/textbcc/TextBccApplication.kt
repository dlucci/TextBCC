package com.dlucci.textbcc

import android.app.Application
import com.dlucci.textbcc.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TextBccApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@TextBccApplication)
            modules(appModule)
        }
    }

}