package com.stevdza_san.demo.data.remote

import com.stevdza_san.demo.domain.Postv
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

class MovieApi(private val client: HttpClient) {

    suspend fun fetchMovies(page: Int): List<Postv> {
        val response: MovieResponse = client.get("https://api.themoviedb.org/3/discover/movie") {
            parameter("api_key", "25a106ac989fe8bfda1dfa5c92602aa4")
            parameter("page", page)
        }.body()
        println("Fetched movies: ${response.results}")

        return response.results
    }

    @Serializable
    data class MovieResponse(val results: List<Postv>)
}