package com.example.movieapp.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.mappers.toMovie
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import retrofit2.HttpException

class MoviePagingSource(
    private val api: KinopoiskApi,
    private val movieCategory: MovieCategory
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val response = api.getCollection(movieCategory, pageNumber)

            if (response.isSuccessful) {
                val movies = response.body()!!.items.map { it.toMovie() }
                val nextPageNumber = if (movies.isEmpty()) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(
                    data = movies,
                    prevKey = prevPageNumber,
                    nextKey = nextPageNumber
                )
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }

    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }
}