package com.stevdza_san.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Firebase.initialize(applicationContext,
            options = FirebaseOptions(
                applicationId = "1:519524875233:android:b416f5e8dafbd1eae430a0",
                apiKey = "AIzaSyAZFLwu7LHwt-NesI2lcEEKUgslWSPWwRU",
                projectId = "kmmm-4727e"

            )
        )

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
   // App(dao =)
}