package com.stevdza_san.demo.data.paging

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.stevdza_san.demo.data.remote.MovieApi
import com.stevdza_san.demo.domain.Postt
import com.stevdza_san.demo.domain.Postv

class MoviePagingSource(private val api: MovieApi) : PagingSource<Int, Postv>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Postv> {
        val page = params.key ?: 1
        return try {
           val movies = api.fetchMovies(page)
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Postv>): Int? = null
}