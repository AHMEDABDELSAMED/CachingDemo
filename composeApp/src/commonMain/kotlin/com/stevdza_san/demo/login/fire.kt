package com.stevdza_san.demo.login
import com.stevdza_san.demo.domain.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.DataSnapshot
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import dev.gitlive.firebase.database.Query
import dev.gitlive.firebase.database.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

import dev.gitlive.firebase.database.database
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class FirebaseRealtimeHelper {

   private val tasksRef = Firebase.database("tasks")

   // قراءة المهام النشطة (غير المكتملة)
   @OptIn(DelicateCoroutinesApi::class)
   fun fetchSupportValue(): Flow<RequestState<String>> = flow {
      val database = Firebase.database
      try {
         // إرسال حالة التحميل أولاً
         emit(RequestState.Loading)

         // الحصول على مرجع لحقل "support" في قاعدة بيانات Firebase
         val supportRef = database.reference("support")
         val snapshot = supportRef.child("support").valueEvents.first()

         // التحقق من أن البيانات موجودة واسترجاعها
         if (snapshot.exists) {
            val value = snapshot.value
            emit(RequestState.Success(value as String))
         } else {
            emit(RequestState.Error("Support field not found"))
         }
      } catch (e: Exception) {
         // التعامل مع الخطأ عند حدوث مشكلة أثناء القراءة من Firebase
         emit(RequestState.Error("Database error: ${e.message}"))
      }
   }


   suspend fun save(username: String) {
      // الحصول على مرجع قاعدة البيانات Firebase
      val database = Firebase.database
      val usersRef = database.reference("users")



      try {
         // محاولة تخزين قيمة "username" في Firebase
         usersRef.child("id").child("username").setValue(username)
         println("add data")

      } catch (exception: Exception) {
         // التعامل مع الأخطاء في حالة حدوث مشكلة أثناء الكتابة في Firebase
         println("Error saving data: ${exception.message}")
      }
   }

}
