package com.example.movieapp.presentation.app.di

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.api.KinopoiskApi
import com.example.movieapp.data.repository.MovieRepositoryImpl
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.presentation.featureHome.HomeViewModel
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsViewModel
import com.example.movieapp.presentation.featureMovieList.MovieListViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val mainModule =
    module {
        single<MovieRepository> {
            MovieRepositoryImpl(get())
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

        viewModel {
            HomeViewModel(get())
        }

        viewModel { (movieCategory: MovieCategory) ->
            MovieListViewModel(movieCategory = movieCategory, movieRepository = get())
        }

        viewModel { (movieId: Int) ->
            MovieDetailsViewModel(movieId = movieId, movieRepository = get())
        }
    }

private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
