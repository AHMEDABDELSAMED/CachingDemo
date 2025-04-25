package com.stevdza_san.demo.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(tableName = "post")
@Serializable
data class Post(
    val userId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String
)