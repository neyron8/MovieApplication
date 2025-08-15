package com.example.movieapplication.navigation

import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.movieapplication.modelsNew.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideosSection(videoUrls: List<Video.Item>?) {
    if (videoUrls.isNullOrEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Видео",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFFBB86FC),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        videoUrls.forEach { videoUrl ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                VideoPlayer(videoUrl = videoUrl.url)
                Log.d("URL", videoUrl.url.toString())
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
        val videoId = extractYouTubeVideoId(videoUrl)
        if (videoId != null) {
            val youTubePlayerView = remember {
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
            }

            AndroidView(
                factory = { youTubePlayerView },
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black),
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
                    youTubePlayerView.release()
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
        val player = remember {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(videoUrl.toString()))
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        errorMessage = "Ошибка воспроизведения: ${error.message}"
                    }
                })
            }
        }

        val playerView = remember {
            PlayerView(context).apply {
                useController = true
                controllerShowTimeoutMs = 5000
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        }

        playerView.player = player

        LaunchedEffect(player) {
            player.prepare()
            player.playWhenReady = false
        }

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp)),
            factory = { playerView }
        )

        DisposableEffect(videoUrl) {
            onDispose {
                player.release()
            }
        }
    }

    errorMessage?.let {
        Text(
            text = it,
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}