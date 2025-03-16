package com.stevdza_san.demo.di

import MainViewModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.russhwolf.settings.Settings
import com.stevdza_san.demo.data.PostSDK
import com.stevdza_san.demo.data.local.DatabaseDriverFactory
import com.stevdza_san.demo.data.local.LocalDatabase
import com.stevdza_san.demo.data.remote.PostApi
import com.stevdza_san.demo.login.LoginViewModel
import com.stevdza_san.demo.room.AppDatabaseConstructor
import com.stevdza_san.demo.ui.LoginScreen
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val targetModule: Module

val sharedModule = module {
    single<PostApi> { PostApi() }
    single<LocalDatabase> { LocalDatabase(databaseDriverFactory = get()) }
    single<Settings> { Settings() }
    //single<AppDatabaseConstructor> { get() }
    //single<AppDatabaseConstructor> { get() }
    single<PostSDK> {
        PostSDK(
            api = get(),
            database = get(),
            settings = get(),
            app = get<AppDatabaseConstructor>()

        )
    }



   // factory { (componentContext: ComponentContext) -> MainViewModel(get(), componentContext) }
   // factory { (componentContext: ComponentContext) -> LoginViewModel( componentContext) }
    single<ComponentContext> { DefaultComponentContext(LifecycleRegistry()) }

    single { LoginViewModel( componentContext = get()) }
    single { MainViewModel(sdk = get(), componentContext = get()) } }

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}