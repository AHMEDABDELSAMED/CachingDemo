package com.stevdza_san.demo.presentation.viewmodel

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.ComponentContext
import com.stevdza_san.demo.data.PostSDK
import com.stevdza_san.demo.data.paging.MoviePagingSource
import com.stevdza_san.demo.data.remote.MovieApi
import com.stevdza_san.demo.data.room.Post
import com.stevdza_san.demo.domain.Postv
import com.stevdza_san.demo.domain.RequestState

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


// ✅ تعريف ViewModel مع Decompose
class MainViewModel(
    private val sdk: PostSDK,
    api: MovieApi,
    componentContext: ComponentContext
) : ComponentContext by componentContext, InstanceKeeper.Instance {

    private val pager = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { MoviePagingSource(api) }
    )

    val moviePagingData: Flow<PagingData<Postv>> = pager.flow

    init {
        println("✅ com.stevdza_san.demo.presentation.viewmodel.MainViewModel initialized")
    }


    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // ✅ الاحتفاظ بالحالة عبر MutableStateFlow
    private val _allPosts = MutableStateFlow<RequestState<List<Post>>>(RequestState.Idle)
    val allPosts: StateFlow<RequestState<List<Post>>> = _allPosts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        scope.launch {
            _allPosts.value = RequestState.Idle

            sdk.getAllPosts().collect { requestState ->
                _allPosts.value = requestState
            }
        }
    }

    override fun onDestroy() {
        scope.cancel() // إيقاف الكوروتينات عند تدمير الـ ViewModel
    }
}
