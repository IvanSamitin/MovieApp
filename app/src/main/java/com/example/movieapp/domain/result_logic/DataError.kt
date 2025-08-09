package com.example.movieapp.domain.result_logic

interface DataError: Error {
    enum class Network: DataError{
        NO_INTERNET

    }
}