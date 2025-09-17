package com.example.movieapp.app.di

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.movieapp.data.di.dataModule
import com.example.movieapp.data.sync.MovieSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import java.util.concurrent.TimeUnit

class MovieApp : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApp)
            modules(mainModule, dataModule)
            workManagerFactory()
        }
        setupPeriodicSync()
    }

    private fun setupPeriodicSync() {
        val workRequest = PeriodicWorkRequestBuilder<MovieSyncWorker>(
            12, TimeUnit.HOURS
        )
//            .setConstraints(
//                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
//            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MovieSyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
