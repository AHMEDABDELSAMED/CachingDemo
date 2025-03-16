package com.stevdza_san.demo.app

import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

object Logic {

    suspend fun add(): Flow<Int> = flow{
        var counter = 0
        while (true) {
            emit(counter++)
            delay(1000)
        }


    }

}


class viewModel(componentContext: ComponentContext)
    : ComponentContext by componentContext , InstanceKeeper.Instance{
       private val _all= MutableStateFlow<List<Int>>(emptyList())
        val all : StateFlow<List<Int>> =_all

        val job= CoroutineScope(SupervisorJob() + Dispatchers.Main)
    fun get(){
        job.launch {
            Logic.add().collect(){it ->
                _all.value +=it
            }
        }
    }


}