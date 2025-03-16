package com.stevdza_san.demo.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Post::class],
    version = 2
)
abstract class PeopleDatabase: RoomDatabase() {

    abstract fun peopleDao(): PeopleDao

}