package com.example.movieapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.data.local.dao.MovieDao
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(get<Context>(), MovieDatabase::class.java, "movie_database").build()
    }

    single {
        get<MovieDatabase>().dao
    }
}