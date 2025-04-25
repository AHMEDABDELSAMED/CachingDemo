package com.stevdza_san.demo.presentation.ui

import com.stevdza_san.demo.presentation.viewmodel.MainViewModel
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.stevdza_san.demo.domain.Postv
import kotlinx.coroutines.flow.Flow
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

class NextHome : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val componentContext: ComponentContext = DefaultComponentContext(LifecycleRegistry())
        val viewModel: MainViewModel = getKoin().get(parameters = { parametersOf(componentContext) })
        val allPosts = viewModel.allPosts.collectAsState().value
        val p=viewModel.moviePagingData

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (allPosts.isSuccess()) {
                val data = allPosts.getSuccessData() // الحصول على البيانات من النجاح

                MovieListScreen(p)
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



    @Composable
    fun MovieListScreen(pager: Flow<PagingData<Postv>>) {
        val lazyPagingItems = pager.collectAsLazyPagingItems()

        LazyColumn {
            items(
                count = lazyPagingItems.itemCount,
                key = { index ->
                    val post = lazyPagingItems[index]
                    "${post?.id}_${index}"
                }
            ) { index ->
                val post = lazyPagingItems[index]
                post?.let {
                    AnimatedProductCard(
                        post = it,
                        isFromRight = index % 2 == 0
                    )
                }
            }
        }
    }

    @Composable
    fun AnimatedProductCard(post: Postv, isFromRight: Boolean) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // ✅ صورة الخلفية
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${post.posterPath}",
                    contentDescription = post.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // ✅ طبقة شفافة لتوضيح النص
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                                startY = 0f,
                                endY = 300f
                            )
                        )
                )


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "(${post.id}) - ${post.title}",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = post.title,
                        color = Color.White
                    )
                }
            }
        }
    }

}
