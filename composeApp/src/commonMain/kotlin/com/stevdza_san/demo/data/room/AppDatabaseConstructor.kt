package com.stevdza_san.demo.data.room

import androidx.room.RoomDatabaseConstructor

expect class AppDatabaseConstructor : RoomDatabaseConstructor<PeopleDatabase> {
    override fun initialize(): PeopleDatabase
}