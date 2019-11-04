package com.ademir.exotransitionin

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ademir.exotransitionin.databinding.RowVideoBinding
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoAdapter(
    private val onClick: (View) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var dataSet: ArrayList<Video> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_video, parent, false)
        return VideoViewHolder(view, onClick)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    fun setData(data: List<Video>) {
        this.dataSet.clear()
        this.dataSet.addAll(data)
        notifyDataSetChanged()
    }

    class VideoViewHolder(
        itemView: View,
        onClick: (View) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = RowVideoBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                onClick(itemView)
            }
        }

        fun bind(item: Video) {
            if (!item.url.isBlank()) {
                // Produces DataSource instances through which media data is loaded.
                val dataSourceFactory =
                    DefaultDataSourceFactory(
                        itemView.context,
                        Util.getUserAgent(itemView.context, "yourApplicationName")
                    )
                // This is the MediaSource representing the media to be played.
                val videoSource: MediaSource =
                    ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(item.url))
                // Prepare the player with the source.
                viewBinding.playerView.tag = item
                viewBinding.playerView.transitionName = PlayerFragment.getTransitionName(item)
                viewBinding.playerView.player =
                    ExoPlayerFactory.newSimpleInstance(itemView.context).apply {
                        playWhenReady = true
                        prepare(videoSource)
                    }
            }

        }
    }
}