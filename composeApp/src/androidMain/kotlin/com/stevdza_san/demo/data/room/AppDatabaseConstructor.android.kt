package com.stevdza_san.demo.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor

actual class AppDatabaseConstructor(private val context: Context) :
    RoomDatabaseConstructor<PeopleDatabase> {

    // إنشاء متغير يحتفظ بنسخة واحدة فقط من قاعدة البيانات
    private var databaseInstance: PeopleDatabase? = null

    actual override fun initialize(): PeopleDatabase {
        if (databaseInstance == null) {
            val appContext = context.applicationContext
            val dbFile = appContext.getDatabasePath("my_room.db")

            databaseInstance = Room.databaseBuilder(
                context = appContext,
                klass = PeopleDatabase::class.java,
                name = dbFile.absolutePath
            ).fallbackToDestructiveMigration() .build()
        }
        return databaseInstance!!
    }
}