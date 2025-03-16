package com.stevdza_san.demo.ui

import MainViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.stevdza_san.demo.data.PostSDK
import com.stevdza_san.demo.room.Post
import kotlinx.coroutines.delay
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class NextHome : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val viewModel: MainViewModel = getKoin().get()
        //val viewModel: MainViewModel = getKoin().get { parametersOf(componentContext) }


        // جمع البيانات باستخدام collectAsState() لمراقبة التغييرات
        val allPosts = viewModel.allPosts.collectAsState().value

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (allPosts.isSuccess()) {
                val data = allPosts.getSuccessData() // الحصول على البيانات من النجاح
                AnimatedPostList(data)
            }
            else if (allPosts.isError()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = allPosts.getErrorMessage(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AnimatedPostList(allPosts: List<Post>) {
        val listState = rememberLazyListState()
        var visibleItems by remember { mutableStateOf(setOf<Int>()) }

        LaunchedEffect(listState.firstVisibleItemIndex) {
            listState.layoutInfo.visibleItemsInfo.forEach { item ->
                visibleItems = visibleItems + item.index
            }
        }

        LazyColumn(state = listState) {
            items(items = allPosts, key = { it.id }) { post ->
                val index = allPosts.indexOf(post)
                val isEven = index % 2 == 0 // تحديد إذا كان العنصر زوجيًا أم فرديًا
                AnimatedProductCard(
                    post = allPosts[index], // استبدل بـ ID الصورة الخاص بك
                    isFromRight = isEven
                )
            }
        }
    }

    @Composable
    fun AnimatedProductCard( post: Post, isFromRight: Boolean) {
        val animationOffset = remember { Animatable(if (isFromRight) 300f else -300f) }

        LaunchedEffect(Unit) {
            animationOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500)
            )
        }

        Card(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(160.dp)
                .offset(x = animationOffset.value.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column {
                Text(
                    text = "(${post.id}) - ${post.title}",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Medium
                )
                Text(text = post.body)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
