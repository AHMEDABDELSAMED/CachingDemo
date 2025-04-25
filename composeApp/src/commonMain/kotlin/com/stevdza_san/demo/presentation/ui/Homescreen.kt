package com.stevdza_san.demo.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import chaintech.videoplayer.host.VideoPlayerHost
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.Uri
import coil3.compose.AsyncImage
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import io.ktor.http.ContentDisposition.Companion.File
import org.jetbrains.compose.ui.tooling.preview.Preview

class Homescreen : Screen {
    @Composable
    override fun Content() {
        var showFilePicker by rememberSaveable { mutableStateOf(false) }
        val fileTypes = listOf("jpg", "png")
        val navigator = LocalNavigator.current
        val playerHost = remember { VideoPlayerHost(url = "https://www.youtube.com/watch?v=1_isaWRA0OE") }
        var selectedImageUri by remember { mutableStateOf<String?>(null) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navigator?.push(NextHome()) }) {
                    Text(text = "Go next")
                }


                Spacer(Modifier.height(16.dp))

                Button(onClick = { showFilePicker = true }) {
                    Text("choose file")
                }
                VideoPlayerComposable(
                    modifier = Modifier.height(200.dp).fillMaxWidth(),
                    playerHost = playerHost
                )

                FilePicker(show = showFilePicker, fileExtensions = fileTypes) { selectedFile ->
                    showFilePicker = false
                    selectedFile?.let {
                        selectedImageUri = it.path
                        println("تم اختيار الملف: ${it.path}")  // يمكنك معالجة الملف هنا
                    }
                }
                selectedImageUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "الصورة المختارة",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                }
            }
        }
    }
}
