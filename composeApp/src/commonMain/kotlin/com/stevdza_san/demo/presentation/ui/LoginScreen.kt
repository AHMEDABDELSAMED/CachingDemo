package com.stevdza_san.demo.presentation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cachingdemo.composeapp.generated.resources.D1
import cachingdemo.composeapp.generated.resources.Res
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.rememberAsyncImagePainter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.stevdza_san.demo.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin


class LoginScreen(): Screen {


    @Composable
    override fun Content() {

        Login()
    }

    @Composable
    fun Login() {
        val navigator = LocalNavigator.current
        //val viewModel = remember { LoginViewModel() }
        val componentContext: ComponentContext = DefaultComponentContext(LifecycleRegistry())
        val viewModel = remember{LoginViewModel( componentContext)}

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var loginResult by remember { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            ShakyButton(onclick = {scope.launch {
                // Execute login in background
                val result = viewModel.register(email, password)
                loginResult = result.toString()
            }}, navigator = navigator!!)
            Spacer(modifier = Modifier.height(16.dp))
           /*
            Button(onClick = {
                navigator?.push(Homescreen())
                /*
                scope.launch {
                    // Execute login in background
                    val result = viewModel.register(email, password)
                    loginResult = result.toString()
                }
                 */
            }) {
                Text("Login")
            }
            */
            loginResult?.let {
                if (it == "true") {
                    // Navigate to HomeScreen
                    navigator?.push(Homescreen())
                }
                Text(text = if (it == "true") "Login successful!" else "Login failed!")
            }
            Spacer(modifier = Modifier.height(16.dp))
            val painter: Painter = rememberAsyncImagePainter(model = "url")
            Image(painter = painterResource(Res.drawable.D1), contentDescription = "Logo",modifier = Modifier.size(100.dp))
        }


    }
    @Composable
    fun ShakyButton(onclick:()->Unit,navigator: Navigator) {
        var isShaking by remember { mutableStateOf(false) }
        val shakeAnimation by animateFloatAsState(
            targetValue = if (isShaking) 10f else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(50, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Shake Animation"
        )

        // تشغيل الاهتزاز لفترة قصيرة ثم إيقافه
        LaunchedEffect(isShaking) {
            if (isShaking) {
                delay(500) // مدة الاهتزاز
                isShaking = false
                navigator?.push(Homescreen())
            }
        }

        Button(
            onClick = {
                isShaking = true
                      onclick()
                      }, // تشغيل الاهتزاز عند الضغط
            modifier = Modifier
                .padding(16.dp)
                .graphicsLayer { translationX = if (isShaking) shakeAnimation else 0f }
        ) {
            Text("اضغط للاهتزاز")
        }
    }


}
