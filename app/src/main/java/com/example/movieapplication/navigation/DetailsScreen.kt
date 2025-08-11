package com.example.movieapplication.navigation

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.movieapplication.MainViewModel
import com.example.movieapplication.R
import com.example.movieapplication.modelsNew.FilmData
import com.example.movieapplication.modelsNew.ScreenShots
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.Player
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun DetailsScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    id: Int
) {
    LaunchedEffect(key1 = id) {
        mainViewModel.getDataById(id)
        mainViewModel.getScreenShotsById(id)
    }

    val state = mainViewModel.listOfStates.value
    val filmData = state.filmData
    val screenShots = state.screenShots
    val scrollState = rememberScrollState()
    val animatedAlpha by animateFloatAsState(
        targetValue = if (filmData != FilmData()) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    val testVideoUrls = listOf(
        "https://trailers.s3.mds.yandex.net/video_original/184153-8067504155845044.mp4",
        "https://www.youtube.com/watch?v=dQw4w9WgXcQ", // YouTube
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", // MP4
        "https://www.youtube.com/watch?v=9bZkp7q19f0",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4A4A4A)),
    ) {
        BackGroundPoster(filmData = filmData)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .alpha(animatedAlpha)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            ForegroundPoster(filmData = filmData)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4A4A4A).copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = nameGiverDet(item = filmData).toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFBB86FC),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Rating(filmData = filmData, modifier = Modifier)
                    TextBuilder(
                        icon = Icons.Filled.Info,
                        title = "Описание:",
                        bodyText = filmData.description.toString()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ScreenshotsSection(screenShots = state.screenShots)
            Spacer(modifier = Modifier.height(16.dp))
            VideosSection(videoUrls = testVideoUrls)
        }
    }
}

@Composable
fun ScreenshotsSection(screenShots: List<ScreenShots.ItemX>?) {
    if (screenShots.isNullOrEmpty()) return

    var selectedScreenshot by remember { mutableStateOf<ScreenShots.ItemX?>(null) }

    Text(
        text = "Скриншоты",
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFFBB86FC),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(screenShots) { screenshot ->
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
                    .clickable { selectedScreenshot = screenshot },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = screenshot.imageUrl ?: screenshot.previewUrl,
                    contentDescription = "Screenshot",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    selectedScreenshot?.let { screenshot ->
        Dialog(
            onDismissRequest = { selectedScreenshot = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.6f)
                    .clickable { selectedScreenshot = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = screenshot.imageUrl ?: screenshot.previewUrl,
                    contentDescription = "Enlarged Screenshot",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun VideosSection(videoUrls: List<String>?) {
    if (videoUrls.isNullOrEmpty()) return

    Text(
        text = "Видео",
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFFBB86FC),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(videoUrls) { videoUrl ->
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .height(240.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent // Прозрачный фон
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp // Убираем тень
                )
            ) {
                VideoPlayer(videoUrl = videoUrl)
            }
        }
    }
}


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrl: String?) {
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isYouTube = videoUrl?.contains("youtube.com") == true || videoUrl?.contains("youtu.be") == true

    if (isYouTube) {
        val videoId = videoUrl?.let { extractYouTubeVideoId(it) }
        if (videoId != null) {
            AndroidView(
                factory = {
                    YouTubePlayerView(context).apply {
                        enableAutomaticInitialization = false
                        initialize(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.cueVideo(videoId, 0f)
                            }
                            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                                errorMessage = "Ошибка YouTube: $error"
                            }
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                update = { view ->
                    view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    })
                }
            )

            DisposableEffect(Unit) {
                onDispose {
                    // Ресурсы YouTubePlayerView освобождаются автоматически
                }
            }
        } else {
            Text(
                text = "Недействительный YouTube URL",
                color = Color(0xFFBB86FC),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    } else {
        val player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl.toString()))
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    errorMessage = "Ошибка воспроизведения: ${error.message}"
                }
            })
        }
        val playerView = PlayerView(context).apply {
            useController = true // Включаем элементы управления
            controllerShowTimeoutMs = 3000
            setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING) // Показ буферизации
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }

        playerView.player = player

        LaunchedEffect(player) {
            player.prepare()
            player.playWhenReady = false
        }

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            factory = { playerView }
        )

        DisposableEffect(videoUrl) {
            onDispose {
                player.release()
            }
        }

    }
}

private fun extractYouTubeVideoId(url: String): String? {
    val regex = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?v=)([^#&?]*)(?:[?&#].*)?".toRegex()
    return regex.find(url)?.groupValues?.get(1)
}

@Composable
fun TextBuilder(icon: ImageVector, title: String, bodyText: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFFBB86FC),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFBB86FC)
            )
        }
        Text(
            text = bodyText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFBB86FC),
            lineHeight = 22.sp
        )
    }
}

@Composable
fun Rating(filmData: FilmData, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Rating",
                tint = Color.Yellow,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = ratingGiverDet(filmData).toString(),
                modifier = Modifier.padding(start = 6.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFBB86FC)
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.time_24),
                contentDescription = "Duration",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "${filmData.filmLength} мин",
                modifier = Modifier.padding(start = 6.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFBB86FC)
            )
        }
    }
}

@Composable
fun ForegroundPoster(filmData: FilmData) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = filmData.posterUrl,
            contentDescription = filmData.shortDescription,
            modifier = Modifier
                .width(260.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        )
    }
}

@Composable
fun BackGroundPoster(filmData: FilmData) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4A4A4A))
    ) {
        AsyncImage(
            model = filmData.posterUrl,
            contentDescription = filmData.description,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.3f),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

fun nameGiverDet(item: FilmData): String? {
    val name = listOf(
        item.nameRu.toString(),
        item.nameEn.toString(),
        item.nameOriginal.toString()
    ).filter { it != "null" }
    return name.first()
}

fun ratingGiverDet(item: FilmData): String =
    item.ratingKinopoisk?.toString() ?: "-"
