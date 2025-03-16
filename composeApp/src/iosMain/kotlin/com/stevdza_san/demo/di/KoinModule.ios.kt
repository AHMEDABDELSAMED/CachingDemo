package com.stevdza_san.demo.di

import com.stevdza_san.demo.data.local.DatabaseDriverFactory
import com.stevdza_san.demo.data.local.IOSDatabaseDriverFactory
import com.stevdza_san.demo.room.AppDatabaseConstructor
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
    single { AppDatabaseConstructor() }
}