package com.stevdza_san.demo.data

import com.russhwolf.settings.Settings
import com.stevdza_san.demo.data.local.LocalDatabase
import com.stevdza_san.demo.data.remote.PostApi

import com.stevdza_san.demo.domain.Postt
import com.stevdza_san.demo.domain.RequestState
import com.stevdza_san.demo.room.AppDatabaseConstructor
import com.stevdza_san.demo.room.Post
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.singleOrNull
import kotlin.time.Duration.Companion.hours

const val FRESH_DATA_KEY = "freshDataTimestamp"
class PostSDK(
    private val api: PostApi,
    private val database: LocalDatabase,
    private val settings: Settings,
    private val app : AppDatabaseConstructor

) {
    //val ap=app.initialize().peopleDao()
    private val db by lazy { app.initialize() } // لا يتم إنشاؤه إلا عند الحاجة
    private val ap by lazy { db.peopleDao() }


    @Throws(Exception::class)


    fun getAllPosts(): Flow<RequestState<List<Post>>> = flow {

        try {

            // حاول قراءة البيانات المخزنة
            val cachedPosts =ap.getAllPeople()
            //val cachedPosts = database.readAllPosts()
            // إذا لم تكن هناك بيانات مخزنة

            if (cachedPosts.isEmpty()) {
                settings.putLong(
                    FRESH_DATA_KEY, Clock.System.now().toEpochMilliseconds()
                )
                // جلب البيانات من API وتخزينها في قاعدة البيانات
                emit(
                    RequestState.Success(
                        api.fetchAllPosts().also {

                            ap.upsert(it)
                            // database.removeAllPosts()
                            //database.insertAllPosts(it)
                        }
                    )
                )
            }
            else {
                // إذا كانت البيانات قديمة
                if (isDataStale()) {
                    settings.putLong(
                        FRESH_DATA_KEY,
                        Clock.System.now().toEpochMilliseconds()
                    )
                    // جلب البيانات من API وتخزينها في قاعدة البيانات
                    emit(
                        RequestState.Success(
                            api.fetchAllPosts().also {

                                ap.upsert(it)
                                // database.insertAllPosts(it)

                            }
                        )
                    )
                } else {
                    // إرسال البيانات المخزنة في قاعدة البيانات
                    emit(RequestState.Success(cachedPosts))
                }
            }

          /*

            emit(RequestState.Success(cachedPosts))
           */
        } catch (e: Exception) {
            // في حال حدوث خطأ، إرسال رسالة الخطأ
            emit(RequestState.Error(e.message.toString()))
        }
    }

    private fun isDataStale(): Boolean {
        val savedTimestamp = Instant.fromEpochMilliseconds(
            settings.getLong(FRESH_DATA_KEY, defaultValue = 0L)
        )
        val currentTimestamp = Clock.System.now()
        val difference =
            if (savedTimestamp > currentTimestamp) savedTimestamp - currentTimestamp
            else currentTimestamp - savedTimestamp
        return difference >= 24.hours
    }
}