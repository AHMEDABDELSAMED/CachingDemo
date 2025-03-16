package com.stevdza_san.demo.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

/*
actual class AppDatabaseConstructor(val context : Context) :
    RoomDatabaseConstructor<PeopleDatabase> {

    actual override fun initialize(): PeopleDatabase {
        return getDatabaseBuilder(context).build()
    }
    fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<PeopleDatabase> {
        val appContext = ctx.applicationContext
        val dbFile = appContext.getDatabasePath("my_room.db")
        return Room.databaseBuilder(
            context = appContext,
            klass = PeopleDatabase::class.java,
            name = dbFile.absolutePath
        )
    }
}
 */
actual class AppDatabaseConstructor(private val context: Context) : RoomDatabaseConstructor<PeopleDatabase> {

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