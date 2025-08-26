package com.example.movieapp.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class MovieSyncWorker(
    private val context: Context,
    private val params: WorkerParameters,
    private val movieRepository: MovieRepository
) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                movieRepository.syncListMovie()
            } catch (e: IOException) {
                return@withContext Result.failure()
            }
            Result.success()
        }
    }
}