package com.example.movieapp.domain.resultLogic

interface DataError : Error {
    enum class Network : DataError {
        NO_INTERNET,
    }
}
