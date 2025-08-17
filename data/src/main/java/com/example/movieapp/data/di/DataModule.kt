package com.example.movieapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.api.KinopoiskApi
import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.repository.MovieRepositoryImpl
import com.example.movieapp.domain.repository.MovieRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {
    single {
        Room.databaseBuilder(get<Context>(), MovieDatabase::class.java, "movie_database").build()
    }

    single {
        get<MovieDatabase>().dao
    }

    single<MovieRepository> {
        MovieRepositoryImpl(get(), get())
    }

    single {
        val interceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логировать запросы/ответы
            }
        val apiKey = BuildConfig.API_KEY

        OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val request =
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("X-API-KEY",apiKey)
                        .build()
                chain.proceed(request)
            }.build()
    }

    single {
        val contentType = "application/json".toMediaType()
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    single {
        get<Retrofit>().create(KinopoiskApi::class.java)
    }
}
private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
