package com.stevdza_san.demo.login
import androidx.lifecycle.ViewModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth

class LoginViewModel  (componentContext: ComponentContext
) : ComponentContext by componentContext, InstanceKeeper.Instance {
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password)
            true // تسجيل الدخول ناجح
        } catch (e: Exception) {
            println("Login failed: ${e.message}")
            false // فشل تسجيل الدخول
        }
    }

    suspend fun register(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password)
            true // التسجيل ناجح
        } catch (e: Exception) {
            println("Registration failed: ${e.message}")
            false // فشل التسجيل
        }
    }
}