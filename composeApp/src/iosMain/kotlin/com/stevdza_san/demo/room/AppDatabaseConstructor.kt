package com.stevdza_san.demo.room

import androidx.room.RoomDatabaseConstructor

actual class AppDatabaseConstructor : RoomDatabaseConstructor<PeopleDatabase> {
    actual override fun initialize(): PeopleDatabase {
        return getDatabaseBuilder().build()
    }
}