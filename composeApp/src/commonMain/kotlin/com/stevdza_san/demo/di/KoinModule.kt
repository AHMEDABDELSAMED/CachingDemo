package com.stevdza_san.demo.di

import com.stevdza_san.demo.presentation.viewmodel.MainViewModel
import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.Settings
import com.stevdza_san.demo.data.PostSDK
import com.stevdza_san.demo.data.local.LocalDatabase
import com.stevdza_san.demo.data.remote.ApiClient
import com.stevdza_san.demo.data.remote.MovieApi
import com.stevdza_san.demo.data.remote.PostApi
import com.stevdza_san.demo.login.LoginViewModel
import com.stevdza_san.demo.data.room.AppDatabaseConstructor
import io.ktor.client.HttpClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

expect val targetModule: Module

val sharedModule = module {
    single<PostApi> { PostApi() }
    single<LocalDatabase> { LocalDatabase(databaseDriverFactory = get()) }
    single<Settings> { Settings() }

    single<PostSDK> {
        PostSDK(
            api = get(),
            database = get(),
            settings = get(),
            app = get<AppDatabaseConstructor>()

        )
    }

    single<HttpClient> { ApiClient.provideClient() }
    single{ MovieApi(get()) }
    single { LoginViewModel( componentContext = get()) }

    factory  { (componentContext: ComponentContext) ->
        MainViewModel(
            sdk = get(),
            api = get(),
            componentContext = componentContext
        )
    }
    }

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}