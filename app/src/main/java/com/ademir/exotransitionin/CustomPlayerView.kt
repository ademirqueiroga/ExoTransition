package com.ademir.exotransitionin

import android.content.Context
import android.media.session.PlaybackState
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.util.Util

class CustomPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PlayerView(context, attrs, defStyleAttr), LifecycleObserver, Player.EventListener {

    private val userAgent = Util.getUserAgent(context, context.packageName)
    // Produces DataSource instances through which media data is loaded.
    private val dataSourceFactory = DefaultDataSourceFactory(context, userAgent)
    private val player: SimpleExoPlayer?
        get() = super.getPlayer() as? SimpleExoPlayer

    lateinit var lifecycle: Lifecycle

    val thumb: ImageView = ImageView(context)

    init {
        thumb.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        thumb.scaleType = ImageView.ScaleType.CENTER_CROP
        overlayFrameLayout?.addView(thumb)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        Log.e(TAG, "onResume()")
        player?.playWhenReady = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        Log.e(TAG, "onPause()")
        player?.playWhenReady = false
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        val alpha = if (playWhenReady && playbackState == Player.STATE_READY) 0F else 1F
        thumb.animate().alpha(alpha).setDuration(300).start()
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Log.e(TAG, "onPlayerError()")
        if (error?.type == ExoPlaybackException.TYPE_SOURCE) {
            val cause = error.sourceException
            if (cause is HttpDataSource.HttpDataSourceException) {
                // An HTTP error occurred.
                val requestDataSpec = cause.dataSpec
                // It's possible to find out more about the error both by casting and by
                // querying the cause.
                if (cause is HttpDataSource.InvalidResponseCodeException) {
                    // Cast to InvalidResponseCodeException and retrieve the response code,
                    // message and headers.
                } else {
                    // Try calling httpError.getCause() to retrieve the underlying cause,
                    // although note that it may be null.
                }

            }
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                (player?.currentTag as? String)?.let { loadVideo(it) }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.e(TAG, "onStop()")
        player?.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.e(TAG, "onDestroy()")
        player?.stop(true)
    }

    override fun setPlayer(player: Player?) {
        when (player) {
            null -> this.player?.removeListener(this)
            else -> player.addListener(this)
        }
        super.setPlayer(player)
    }

    fun loadVideo(videoUri: String, resetPosition: Boolean = true) {
        Log.e(TAG, "loadVideo()")
        if (videoUri != player?.currentTag || resetPosition) {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .setTag(videoUri)
                .createMediaSource(videoUri.toUri())
            val resetState = player?.currentTag == videoUri
            player?.prepare(mediaSource, resetPosition, resetState)
        }
    }

    companion object {
        const val TAG = "CustomPlayerView"
    }

}