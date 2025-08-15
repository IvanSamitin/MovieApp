package com.example.movieapp.app.di

import android.app.Application
import com.example.movieapp.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MovieApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApp)
            modules(mainModule, dataModule)

        }
    }
}
