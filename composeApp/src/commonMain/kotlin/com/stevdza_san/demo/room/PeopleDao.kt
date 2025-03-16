package com.stevdza_san.demo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PeopleDao {

    @Upsert
    suspend fun upsert(person: List<Post>)

    @Delete
    suspend fun delete(person: Post)

    @Query("SELECT * FROM post")
     suspend fun getAllPeople(): List<Post>

}